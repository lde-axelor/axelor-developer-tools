package com.axelor.apps.devtools.tools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileTools {

  private static final Logger LOG = LoggerFactory.getLogger(FileTools.class);
  private static final String[] OUTPUT_NAME_SEARCH_LIST =
      new String[] {"*", "\"", "/", "\\", "?", "%", ":", "|", "<", ">", "#"};
  private static final String[] OUTPUT_NAME_REPLACEMENT_LIST =
      new String[] {"_", "'", "_", "_", "_", "_", "_", "_", "_", "_", "_"};

  private FileTools() {}

  public static String formatFileName(String fileName) {
    return StringUtils.replaceEach(fileName, OUTPUT_NAME_SEARCH_LIST, OUTPUT_NAME_REPLACEMENT_LIST);
  }

  /**
   * An utility method to zip a list of files or metaFiles.
   *
   * @param zipFileName the name of the final zip file.
   * @param fileList the list of files.
   * @return a zip file containing all the given list.
   */
  public static Optional<Path> zip(String zipFileName, List<File> fileList) {
    try {
      if (CollectionUtils.isEmpty(fileList)) {
        return Optional.empty();
      }
      File zipFile = FileExportTools.getExportFile(zipFileName + ".zip");
      if (Files.deleteIfExists(zipFile.toPath())) {
        LOG.debug("Existing zip deleted.");
      }
      try (ZipOutputStream zout = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
        for (File file : fileList) {
          zout.putNextEntry(new ZipEntry(file.getName()));
          zout.write(IOUtils.toByteArray(Files.newInputStream(file.toPath())));
          if (Files.deleteIfExists(file.toPath())) {
            LOG.debug("file deleted.");
          }
          zout.closeEntry();
        }
      }
      return Optional.of(FileExportTools.relativeToExport(zipFile.toPath()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
