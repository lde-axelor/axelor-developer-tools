package com.axelor.apps.devtools.web.metafieldremover;

import com.axelor.apps.devtools.db.MetaFieldRemover;
import com.axelor.apps.devtools.service.metafieldremover.MetaFieldRemoverService;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaField;
import com.axelor.meta.db.repo.MetaFieldRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class MetaFieldRemoverController {
  public void fetchOrphans(ActionRequest request, ActionResponse response) {
    try {
      Set<MetaField> metaFieldSet = Beans.get(MetaFieldRemoverService.class).fetchOrphans();
      response.setValue(
          "metaFieldSet", metaFieldSet.stream().map(Mapper::toMap).collect(Collectors.toList()));
    } catch (Exception e) {
      response.setError(e.getLocalizedMessage());
    }
  }

  public void removeSelected(ActionRequest request, ActionResponse response) {
    try {
      MetaFieldRemover metaFieldRemover = request.getContext().asType(MetaFieldRemover.class);
      MetaFieldRepository metaFieldRepository = Beans.get(MetaFieldRepository.class);
      int removedEntitiesNbr =
          Beans.get(MetaFieldRemoverService.class)
              .removeSelected(
                  metaFieldRemover.getMetaFieldSet().stream()
                      .filter(Model::isSelected)
                      .map(it -> metaFieldRepository.find(it.getId()))
                      .collect(Collectors.toList()));
      response.setReload(true);
      response.setNotify(String.format(I18n.get("%d entities removed."), removedEntitiesNbr));
    } catch (Exception e) {
      response.setError(e.getLocalizedMessage());
    }
  }

  public void generateSql(ActionRequest request, ActionResponse response) {
    try {
      MetaFieldRemover metaFieldRemover = request.getContext().asType(MetaFieldRemover.class);
      MetaFieldRepository metaFieldRepository = Beans.get(MetaFieldRepository.class);
      Path path =
          Beans.get(MetaFieldRemoverService.class)
              .generateSql(
                  metaFieldRemover.getMetaFieldSet().stream()
                      .filter(Model::isSelected)
                      .map(it -> metaFieldRepository.find(it.getId()))
                      .collect(Collectors.toList()));
      response.setExportFile(path.toString());
    } catch (Exception e) {
      response.setError(e.getLocalizedMessage());
    }
  }
}
