package com.axelor.apps.devtools.service.jpqlquery;

import com.axelor.apps.devtools.db.JpqlQuery;
import com.axelor.db.JpaRepository;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.db.mapper.Property;
import com.axelor.db.mapper.PropertyType;
import com.axelor.i18n.I18n;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class JpqlQueryServiceImpl implements JpqlQueryService {

  @Override
  public String run(JpqlQuery jpqlQuery) throws ClassNotFoundException {
    Class<? extends Model> klass =
        Class.forName(jpqlQuery.getMetaModel().getFullName()).asSubclass(Model.class);
    JpaRepository<? extends Model> repository = JpaRepository.of(klass);
    List<? extends Model> models =
        repository.all().filter(jpqlQuery.getFilter()).order("id").fetch();
    StringJoiner stringJoiner = new StringJoiner("\n");
    Mapper mapper = Mapper.of(klass);
    stringJoiner.add(String.format(I18n.get("Number of results : %s"), models.size()));
    stringJoiner.add(getHeaders(mapper));
    models.forEach(it -> stringJoiner.add(format(mapper, it)));
    return stringJoiner.toString();
  }

  protected String getHeaders(Mapper mapper) {
    StringJoiner stringJoiner = new StringJoiner(" | ");
    stringJoiner.add("\"id\"");
    for (Property property : mapper.getProperties()) {
      if (property.getType().equals(PropertyType.STRING)) {
        stringJoiner.add(String.format("\"%s\"", property.getName()));
      }
    }
    return stringJoiner.toString();
  }

  protected String format(Mapper mapper, Model model) {
    StringJoiner stringJoiner = new StringJoiner(" | ");
    stringJoiner.add(model.getId().toString());
    for (Property property : mapper.getProperties()) {
      if (property.getType().equals(PropertyType.STRING)) {
        stringJoiner.add(
            String.format("\"%s\"", Objects.toString(property.get(model)).replace("\"", "\\\"")));
      }
    }
    return stringJoiner.toString();
  }
}
