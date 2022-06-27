package com.axelor.apps.devtools.service.metafieldremover;

import com.axelor.apps.devtools.tools.FileExportTools;
import com.axelor.apps.devtools.tools.FileTools;
import com.axelor.db.JPA;
import com.axelor.db.mapper.Mapper;
import com.axelor.meta.db.MetaField;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaFieldRepository;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  @Override
  public Path generateSql(List<MetaField> metaFields) throws IOException {
    Path path = Files.createTempFile("MetaFieldDeletion-", ".sql");
    FileTools.setPermissionsSafe(path);
    try (PrintWriter pw = new PrintWriter(path.toFile())) {
      for (MetaField metaField : metaFields) {
        try {
          appendMetaFieldDeletion(pw, metaField);
        } catch (Exception e) {
          log.error(e.getLocalizedMessage());
        }
      }
    }
    return FileExportTools.toExport(path.toFile().getName(), path, true);
  }

  protected void appendMetaFieldDeletion(PrintWriter pw, MetaField metaField) {
    pw.println(
        String.format(
            "DELETE FROM base_advanced_export_line WHERE meta_field = %s;", metaField.getId()));
    pw.println(String.format("DELETE FROM meta_field WHERE id = %s;", metaField.getId()));
  }
}
