package com.axelor.apps.devtools.service.groovyscript;

import com.axelor.apps.devtools.db.GroovyScript;

public interface GroovyScriptService {
  String run(GroovyScript groovyScript) throws ClassNotFoundException;
}
