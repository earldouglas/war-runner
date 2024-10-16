[![Build status](https://github.com/earldouglas/war-runner/workflows/build/badge.svg)](https://github.com/earldouglas/war-runner/actions)
[![Latest version](https://img.shields.io/github/tag/earldouglas/war-runner.svg)](https://repo1.maven.org/maven2/com/earldouglas/war-runner/)

# WAR Runner

WAR Runner builds on
[webapp-runner](https://github.com/heroku/webapp-runner) to support
serving a webapp directly from its components (resources, classes, and
libraries) without needing to packag a .war file.

## Features

* Run a servlet-based webapp without packaging a .war file

## Requirements

* Java 11 and up

## Getting help

* Submit a question, bug report, or feature request as a [new GitHub
  issue](https://github.com/earldouglas/war-runner/issues/new)

## Versioning

WAR Runner follows `com.heroku:webapp-runner` versions, appending an
additional number for its own versioning.

For example, `war-runner` version `10.1.28.0.x` tracks `webapp-runner`
version `10.1.28.0`, which tracks Tomcat version `10.1.28`.
