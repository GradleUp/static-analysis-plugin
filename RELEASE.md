# Releasing

1. Bump version code in `gradle.properties`.
1. Create an entry in `CHANGELOG.md` containing the changes in that release.
1. Create a tag with the same version and push it to origin.
1. After the release is successful do a manual [github release](https://github.com/novoda/gradle-static-analysis-plugin/releases) with the newly created tag. 

This releases the plugin to [bintray](https://bintray.com/GradleUp/maven/static-analysis-plugin) and the [Gradle Plugins Repository](https://plugins.gradle.org/plugin/com.gradleup.static-analysis).
