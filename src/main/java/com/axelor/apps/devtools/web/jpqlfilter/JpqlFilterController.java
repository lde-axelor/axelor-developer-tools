package com.axelor.apps.devtools.web.jpqlfilter;

import com.axelor.apps.devtools.db.JpqlFilter;
import com.axelor.apps.devtools.service.jpqlfilter.JpqlFilterService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class JpqlFilterController {
  public void run(ActionRequest request, ActionResponse response) {
    try {
      JpqlFilter jpqlFilter = request.getContext().asType(JpqlFilter.class);
      response.setValue("$result", Beans.get(JpqlFilterService.class).run(jpqlFilter));
    } catch (Exception e) {
      response.setValue("$result", String.format("%s%n%s", e, e.getCause()));
    }
  }
}
