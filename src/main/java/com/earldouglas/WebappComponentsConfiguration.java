package com.earldouglas;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.FileResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WebappComponentsConfiguration {

  public final String hostname;
  public final int port;
  public final String contextPath;
  public final File emptyWebappDir;
  public final File emptyClassesDir;
  public final Map<String, File> resourceMap;

  public static WebappComponentsConfiguration load(
      final String configurationFilename
  ) throws Exception {

    final URI configurationFileUri =
      WebappComponentsConfiguration
        .class
        .getClassLoader()
        .getResource(configurationFilename)
        .toURI();

    final File configurationFile =
      new File(configurationFileUri);

    return WebappComponentsConfiguration.load(configurationFile);
  }

  private static Map<String, File> parseResourceMap(
      final String raw
  ) throws IOException {

    final Map<String, File> resourceMap =
      new HashMap<String, File>();

    final String[] rows =
      raw.split(",");

    for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
      final String[] columns =
        rows[rowIndex].split("->");
      resourceMap.put(columns[0], new File(columns[1]));
    }

    return resourceMap;
  }

  public static WebappComponentsConfiguration load(
      final File configurationFile
  ) throws IOException {

    final InputStream inputStream =
      new FileInputStream(configurationFile);

    final Properties properties = new Properties();
    properties.load(inputStream);

    final Map<String, File> resourceMap =
      parseResourceMap(properties.getProperty("resourceMap"));

    return new WebappComponentsConfiguration(
      properties.getProperty("hostname"),
      Integer.parseInt(properties.getProperty("port")),
      properties.getProperty("contextPath"),
      new File(properties.getProperty("emptyWebappDir")),
      new File(properties.getProperty("emptyClassesDir")),
      resourceMap
    );
  }

  public WebappComponentsConfiguration(
      final String hostname,
      final int port,
      final String contextPath,
      final File emptyWebappDir,
      final File emptyClassesDir,
      final Map<String, File> resourceMap
  ) {
    this.hostname = hostname;
    this.port = port;
    this.contextPath = contextPath;
    this.emptyWebappDir = emptyWebappDir;
    this.emptyClassesDir = emptyClassesDir;
    this.resourceMap = resourceMap;
  }
}
