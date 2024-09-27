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
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/** Launches a webapp composed of in-place resources, classes, and
  * libraries.
  *
  * emptyWebappDir and emptyClassesDir need to point to one or two empty
  * directories. They're not used to serve any content, but they are
  * required by Tomcat's internals.
  *
  * To use a root context path (i.e. /), set contextPath to the empty
  * string for some reason.
  */
public class WebappComponentsRunner {

  public static File mkdir(final File file) throws IOException {
    if (file.exists()) {
      if (!file.isDirectory()) {
        throw new FileAlreadyExistsException(file.getPath());
      } else {
        return file;
      }
    } else {
      final Path path =
        FileSystems
          .getDefault()
          .getPath("target", "empty");
      try {
        Files.createDirectory(path);
        return file;
      } catch (FileAlreadyExistsException e) {
        return file;
      }
    }
  }

  public final Runnable start;
  public final Runnable join;
  public final Runnable stop;

  public WebappComponentsRunner(
      final String hostname,
      final int port,
      final String contextPath,
      final File emptyWebappDir,
      final File emptyClassesDir,
      final Map<String, File> resourceMap
  ) {

    final Tomcat tomcat = new Tomcat();
    tomcat.setHostname(hostname);

    final Connector connector = new Connector();
    connector.setPort(port);
    tomcat.setConnector(connector);

    final Context context =
      tomcat
        .addWebapp(
          contextPath,
          emptyWebappDir.getAbsolutePath()
        );

    // Set ClassLoader to prevent `ClassNotFoundException: ServletDef`
    // See https://coderanch.com/t/615403/application-servers/Starting-Web-App-Embeded-Tomcat
    context.setParentClassLoader(getClass().getClassLoader());

    final WebResourceRoot webResourceRoot =
      new StandardRoot(context);

    webResourceRoot.addJarResources(
      new DirResourceSet(
        webResourceRoot,
        "/WEB-INF/classes",
        emptyClassesDir.getAbsolutePath(),
        "/"
      )
    );

    resourceMap
      .forEach((path, file) -> {
        if (file.exists() && file.isFile()) {
          webResourceRoot.addJarResources(
            new FileResourceSet(
              webResourceRoot,
              "/" + path,
              file.getAbsolutePath(),
              "/"
            )
          );
        }
      });

    context.setResources(webResourceRoot);

    start =
      new Runnable() {
        @Override
        public void run() {
          try {
            tomcat.start();
          } catch (final LifecycleException e) {
            throw new RuntimeException(e);
          }
        }
      };
    
    join =
      new Runnable() {
        @Override
        public void run() {
          tomcat.getServer().await();
        }
      };
    
    stop =
      new Runnable() {
        @Override
        public void run() {
          try {

            connector.stop();
            context.stop();
            tomcat.stop();

            connector.destroy();
            context.destroy();
            tomcat.destroy();

          } catch (final LifecycleException e) {
            throw new RuntimeException(e);
          }
        }
      };
  }
}
