package com.axelor.apps.devtools.service.javaclassgenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface JavaClassGeneratorService {
  Optional<Path> convertJsonToJavaClass(String inputJson) throws IOException;
}
