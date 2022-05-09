package com.axelor.apps.devtools.service.groovyscript;

import com.axelor.apps.devtools.db.GroovyScript;
import com.axelor.db.JpaRepository;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.rpc.Context;
import com.axelor.script.GroovyScriptHelper;
import org.apache.commons.lang3.text.WordUtils;
import java.util.Objects;

public class GroovyScriptServiceImpl implements GroovyScriptService {

  @Override
  public String run(GroovyScript groovyScript) throws ClassNotFoundException {
    Class<? extends Model> klass =
        Class.forName(groovyScript.getMetaModel().getFullName()).asSubclass(Model.class);
    JpaRepository<? extends Model> repository = JpaRepository.of(klass);
    Model model = repository.find(groovyScript.getId());
    GroovyScriptHelper groovyScriptHelper =
        new GroovyScriptHelper(new Context(Mapper.toMap(model), klass));
    return WordUtils.wrap(Objects.toString(groovyScriptHelper.eval(groovyScript.getScript())), 180);
  }
}
