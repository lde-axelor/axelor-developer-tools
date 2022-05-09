package com.axelor.apps.devtools.service.jpqlquery;

import com.axelor.apps.devtools.db.JpqlQuery;

public interface JpqlQueryService {
  String run(JpqlQuery jpqlQuery) throws ClassNotFoundException;
}
