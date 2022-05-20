package com.axelor.apps.devtools.web.javaclassgenerator;

import com.axelor.apps.devtools.db.JavaClassGenerator;
import com.axelor.apps.devtools.service.javaclassgenerator.JavaClassGeneratorService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.nio.file.Path;
import java.util.Optional;

public class JavaClassGeneratorController {
  public void run(ActionRequest request, ActionResponse response) {
    try {
      JavaClassGenerator javaClassGenerator = request.getContext().asType(JavaClassGenerator.class);
      Optional<Path> optionalPath =
          Beans.get(JavaClassGeneratorService.class)
              .convertJsonToJavaClass(javaClassGenerator.getJson());
      optionalPath.ifPresent(path -> response.setExportFile(path.toString()));
    } catch (Exception e) {
      response.setError(e.getLocalizedMessage());
    }
  }
}
