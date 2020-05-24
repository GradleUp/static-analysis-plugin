# Detekt
[Detekt](https://github.com/arturbosch/detekt) is a static analysis tool that looks for potential bugs and style violations in Kotlin code. It
does not support Java. It can be used in both pure Kotlin, and Android Kotlin projects. It then only makes sense to have Detekt enabled if you
have Kotlin code in your project. The plugin only runs Detekt on projects that contain the Kotlin or the Kotlin-Android plugin.

> Supported Detekt Gradle Plugin version: **1.0.0.RC9.2 and above** 
> Last tested Detekt Gradle Plugin version: **1.3.1**

## Table of contents
 * [IMPORTANT: setup Detekt](#important-setup-detekt)
 * [Configure Detekt](#configure-detekt)
 * [Exclude files from Detekt analysis](#exclude-files-from-detekt-analysis)
 * [Detekt in mixed-language projects](#detekt-in-mixed-language-projects)

---

## IMPORTANT: setup Detekt
Unlike the other tools, the plugin **won't automatically add Detekt** to your project. If you forget to do it, the plugin will fail the build
with an error.

In order to use Detekt, you need to manually add it to **all** your Kotlin projects. You can refer to the
[official documentation](https://github.com/arturbosch/detekt/#gradlegroovy) for further details. Note that you should _not_ add the `detekt`
closure to your `build.gradle`s, unlike what the official documentation says. The `detekt` closure in the `staticAnalysis` configuration gets
applied to all Kotlin modules automatically.

In most common cases, adding Detekt to a project boils down to three simple steps:

 1. Add this statement to your root `build.gradle` project (change the version according to your needs):
    ```gradle
    plugins {
        id 'io.gitlab.arturbosch.detekt' version '1.0.0-RC14'
        // ...
    }
    ```
 2. Add this statement to each Kotlin project's `build.gradle`s:
    ```gradle
    plugins {
        id 'io.gitlab.arturbosch.detekt'
        // ...
    }
    ```

## Configure Detekt
To enable and configure Detekt for a project use the `detekt` closure. The closure behaves exactly like the
[standard Detekt plugin](https://github.com/arturbosch/detekt#with-gradle) does in Gradle, which is to say, quite differently
from how the other tools' configurations closures work. For example:

```gradle
detekt {
    toolVersion = "[version]" // custom toolVersion defined. By default, it is the Gradle plugin version
    input = files("src/main/kotlin") // Optional: files representing project's Kotlin sources
    baseline = file("my-detekt-baseline.xml") // Optional: Just if you want to create a baseline file.
}
```

You need to provide **at a minimum** the `config` and `output` values. It's important that you do _not_ specify a `warningThreshold` nor a `failThreshold`
in the Detekt configuration file as it will interfere with the functioning of the Static Analysis plugin's threshold counting. For the same reason, make
sure that `failFast` is set to `false` in the Detekt configuration.

For more information about Detekt rules, refer to the [official website](https://arturbosch.github.io/detekt/#quick-start-with-gradle).

## Detekt in mixed-language projects
If your project mixes Java and Kotlin code, you don't need to have an exclusion in place for all `*.java` files. Detekt itself only looks for
`*.kt` files, so no further configuration is required.
