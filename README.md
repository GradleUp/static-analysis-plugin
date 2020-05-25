# Gradle static analysis plugin
![Test](https://github.com/GradleUp/static-analysis-plugin/workflows/Test/badge.svg)
[![](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)](LICENSE.txt)

A Gradle plugin to easily apply the same setup of static analysis tools across different Android, Java or Kotlin projects.

Supports [Task Configuration Avoidance](https://docs.gradle.org/current/userguide/task_configuration_avoidance.html) so that you have zero overhead in build speeds when you use this plugin!

## Description
Gradle supports many popular static analysis (Checkstyle, PMD, SpotBugs, etc) via a set of built-in plugins.
Using these plugins in an Android module will require an additional setup to compensate for the differences between
the model adopted by the Android plugin compared to the Java one.

The `static-analysis-plugin` aims to provide:
- flexible, configurable penalty strategy for builds
- easy, Android-friendly integration for all static analysis
- convenient way of sharing same setup across different projects
- healthy, versionable and configurable defaults

### Supported tools
The plugin supports various static analysis tools for Java, Kotlin and Android projects:

 * [`Checkstyle`](docs/tools/checkstyle.md)
 * [`PMD`](docs/tools/pmd.md)
 * [`SpotBugs`](docs/tools/spotbugs.md)
 * [`Detekt`](docs/tools/detekt.md)
 * [`Android Lint`](docs/tools/android_lint.md)
 * [`KtLint`](docs/tools/ktlint.md)
 
Please note that the tools availability depends on the project the plugin is applied to. For more details please refer to the
[supported tools](docs/supported-tools.md) page.

### Tools in-consideration
                          
 * `CPD (Duplicate Code Detection) `
 * `error-prone`
 * `Jetbrains IDEA Inspections`

For all tools in consideration, please refer to [issues](https://github.com/GradleUp/static-analysis-plugin/issues?q=is%3Aissue+is%3Aopen+label%3A%22new+tool%22). 

### Out-of-the-box support for Android projects
Android projects use a Gradle model that is not compatible with the Java one, supported by the built-in static analysis tools plugins.
Applying `com.gradleup.static-analysis` Plugin to your Android project will make sure all the necessary tasks are created and correctly configured
without any additional hassle.

## Add the plugin to your project
        
[ ![Bintray](https://img.shields.io/bintray/v/gradleup/maven/static-analysis-plugin) ](https://bintray.com/gradleup/maven/static-analysis-plugin/_latestVersion)
[ ![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/com/gradleup/static-analysis/com.gradleup.static-analysis.gradle.plugin/maven-metadata.xml.svg?label=Gradle%20Plugins%20Portal) ](https://plugins.gradle.org/plugin/com.gradleup.static-analysis)

Add the plugin in `build.gradle(.kts)` file

```kotlin
plugins {
    id("com.gradleup.static-analysis") version "<latest-version>"
}
```   

Or apply the plugin from jcenter as a classpath dependency

```groovy
buildscript {
    repositories {
       jcenter()
    }
    dependencies {
        classpath("com.gradleup:static-analysis-plugin:<latest-version>")
    }
}

apply plugin: 'com.gradleup.static-analysis'
```

## Simple usage
A typical configuration for the plugin will look like:

```gradle
staticAnalysis {
    penalty {
        maxErrors = 0
        maxWarnings = 0
    }
    checkstyle { }
    pmd { }
    spotbugs { }
    detekt { }
    lintOptions { }
}
```

This will enable all the tools with their default settings and create `evaluateViolations` task. Running `./gradlew evaluateViolations` task will run all configured tools and print the reports to console. For more advanced configurations, please refer to the
[advanced usage](docs/advanced-usage.md) and to the [supported tools](docs/supported-tools.md) pages.

## Sample app
There are two sample Android projects available, one consisting of a regular app - available [here](https://github.com/GradleUp/static-analysis-plugin/tree/master/sample) - and the other comprising a multi-module setup available [here](https://github.com/GradleUp/static-analysis-plugin/tree/master/sample-multi-module). Both sample projects showcase a setup featuring Checkstyle, SpotBugs, PMD, Lint, Ktlint and Detekt.

## License 

> This project is forked from its original location by the original authors https://github.com/novoda/gradle-static-analysis-plugin 

Copyright 2020 The GradleUp Authors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
