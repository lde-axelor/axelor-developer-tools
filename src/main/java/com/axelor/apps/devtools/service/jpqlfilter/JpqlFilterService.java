package com.axelor.apps.devtools.service.jpqlfilter;

import com.axelor.apps.devtools.db.JpqlFilter;

public interface JpqlFilterService {
  String run(JpqlFilter jpqlFilter) throws ClassNotFoundException;
}
