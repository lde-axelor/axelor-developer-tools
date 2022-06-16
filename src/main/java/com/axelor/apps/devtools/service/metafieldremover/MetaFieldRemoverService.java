package com.axelor.apps.devtools.service.metafieldremover;

import com.axelor.meta.db.MetaField;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface MetaFieldRemoverService {
  Set<MetaField> fetchOrphans();

  int removeSelected(List<MetaField> metaFields);

  Path generateSql(List<MetaField> metaFields) throws IOException;
}
