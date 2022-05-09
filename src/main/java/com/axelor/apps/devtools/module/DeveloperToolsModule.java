package com.axelor.apps.devtools.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.devtools.service.groovyscript.GroovyScriptService;
import com.axelor.apps.devtools.service.groovyscript.GroovyScriptServiceImpl;
import com.axelor.apps.devtools.service.jpqlquery.JpqlQueryService;
import com.axelor.apps.devtools.service.jpqlquery.JpqlQueryServiceImpl;

public class DeveloperToolsModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(JpqlQueryService.class).to(JpqlQueryServiceImpl.class);
    bind(GroovyScriptService.class).to(GroovyScriptServiceImpl.class);
  }
}
