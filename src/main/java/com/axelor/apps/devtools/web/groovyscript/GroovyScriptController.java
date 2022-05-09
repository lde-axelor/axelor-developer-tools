package com.axelor.apps.devtools.web.groovyscript;

import com.axelor.apps.devtools.db.GroovyScript;
import com.axelor.apps.devtools.service.groovyscript.GroovyScriptService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class GroovyScriptController {
  public void run(ActionRequest request, ActionResponse response) {
    try {
      GroovyScript groovyScript = request.getContext().asType(GroovyScript.class);
      response.setValue("$result", Beans.get(GroovyScriptService.class).run(groovyScript));
    } catch (Exception e) {
      response.setValue(
          "$result",
          e.getCause() == null
              ? e.getMessage()
              : String.format("%s%n%s", e.getMessage(), e.getCause()));
    }
  }
}
