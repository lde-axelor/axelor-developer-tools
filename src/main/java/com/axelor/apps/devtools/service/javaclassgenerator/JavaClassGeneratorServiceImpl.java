package com.axelor.apps.devtools.service.javaclassgenerator;

import com.axelor.app.AppSettings;
import com.axelor.apps.devtools.tools.FileTools;
import com.sun.codemodel.JCodeModel;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;

public class JavaClassGeneratorServiceImpl implements JavaClassGeneratorService {
  private static final String DEFAULT_EXPORT_DIR = "{java.io.tmpdir}/axelor/data-export";
  private static final String EXPORT_PATH =
      AppSettings.get().getPath("data.export.dir", DEFAULT_EXPORT_DIR);

  @Override
  public Optional<Path> convertJsonToJavaClass(String inputJson) throws IOException {
    JCodeModel jcodeModel = new JCodeModel();

    GenerationConfig config =
        new DefaultGenerationConfig() {
          @Override
          public boolean isGenerateBuilders() {
            return true;
          }

          @Override
          public SourceType getSourceType() {
            return SourceType.JSON;
          }
        };

    SchemaMapper mapper =
        new SchemaMapper(
            new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()),
            new SchemaGenerator());
    mapper.generate(jcodeModel, "Generated", "com.axelor.json", inputJson);

    File dir = Paths.get(EXPORT_PATH).toFile();
    jcodeModel.build(dir);

    FileFilter fileFilter = new WildcardFileFilter("*.java");
    File[] files =
        dir.toPath().resolve(Paths.get("com/axelor/json")).toFile().listFiles(fileFilter);
    if (files == null || files.length == 0) {
      return Optional.empty();
    }
    return FileTools.zip("generatedClasses", Arrays.asList(files));
  }
}
