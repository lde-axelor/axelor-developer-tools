package com.axelor.apps.devtools.service.metafieldremover;

import com.axelor.meta.db.MetaField;
import java.util.List;
import java.util.Set;

public interface MetaFieldRemoverService {
  Set<MetaField> fetchOrphans();

  int removeSelected(List<MetaField> metaFields);
}
