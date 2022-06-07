package com.axelor.apps.devtools.service.metafieldremover;

import com.axelor.db.JPA;
import com.axelor.db.mapper.Mapper;
import com.axelor.meta.db.MetaField;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaFieldRepository;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MetaFieldRemoverServiceImpl implements MetaFieldRemoverService {

  protected final Logger log = LoggerFactory.getLogger(MetaFieldRemoverServiceImpl.class);
  protected final MetaModelRepository metaModelRepository;
  protected final MetaFieldRepository metaFieldRepository;

  @Inject
  public MetaFieldRemoverServiceImpl(
      MetaModelRepository metaModelRepository, MetaFieldRepository metaFieldRepository) {
    this.metaModelRepository = metaModelRepository;
    this.metaFieldRepository = metaFieldRepository;
  }

  @Override
  public Set<MetaField> fetchOrphans() {
    Set<MetaField> metaFieldSet = new HashSet<>();
    for (MetaModel metaModel : metaModelRepository.all().fetch()) {
      try {
        Mapper mapper = Mapper.of(Class.forName(metaModel.getFullName()));
        for (MetaField metaField : metaModel.getMetaFields()) {
          if (mapper.getProperty(metaField.getName()) == null) {
            metaFieldSet.add(metaField);
          }
        }
      } catch (ClassNotFoundException e) {
        log.error("Class {} not found.", metaModel.getFullName());
      }
    }
    return metaFieldSet;
  }

  @Override
  public int removeSelected(List<MetaField> metaFields) {
    int removedEntities = 0;
    for (MetaField metaField : metaFields) {
      try {
        removeMetaField(metaField);
        removedEntities++;
      } catch (Exception e) {
        log.error(e.getLocalizedMessage());
      }
    }
    return removedEntities;
  }

  @Transactional(rollbackOn = Exception.class)
  protected void removeMetaField(MetaField metaField) {
    JPA.em()
        .createQuery("DELETE FROM AdvancedExportLine WHERE metaField = :_metaField")
        .setParameter("_metaField", metaField)
        .executeUpdate();
    metaFieldRepository.remove(metaField);
  }
}
