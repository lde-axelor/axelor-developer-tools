package com.axelor.apps.devtools.web.jpqlquery;

import com.axelor.apps.devtools.db.JpqlQuery;
import com.axelor.apps.devtools.service.jpqlquery.JpqlQueryService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class JpqlQueryController {
  public void run(ActionRequest request, ActionResponse response) {
    try {
      JpqlQuery jpqlQuery = request.getContext().asType(JpqlQuery.class);
      response.setValue("$result", Beans.get(JpqlQueryService.class).run(jpqlQuery));
    } catch (Exception e) {
      response.setValue("$result", String.format("%s%n%s", e, e.getCause()));
    }
  }
}
