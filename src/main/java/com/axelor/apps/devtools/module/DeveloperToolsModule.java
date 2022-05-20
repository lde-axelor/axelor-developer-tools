package com.axelor.apps.devtools.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.devtools.service.groovyscript.GroovyScriptService;
import com.axelor.apps.devtools.service.groovyscript.GroovyScriptServiceImpl;
import com.axelor.apps.devtools.service.javaclassgenerator.JavaClassGeneratorService;
import com.axelor.apps.devtools.service.javaclassgenerator.JavaClassGeneratorServiceImpl;
import com.axelor.apps.devtools.service.jpqlfilter.JpqlFilterService;
import com.axelor.apps.devtools.service.jpqlfilter.JpqlFilterServiceImpl;

public class DeveloperToolsModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(JpqlFilterService.class).to(JpqlFilterServiceImpl.class);
    bind(GroovyScriptService.class).to(GroovyScriptServiceImpl.class);
    bind(JavaClassGeneratorService.class).to(JavaClassGeneratorServiceImpl.class);
  }
}
