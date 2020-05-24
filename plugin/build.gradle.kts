plugins {
    id("java-gradle-plugin")
    id("groovy")
    id("com.gradle.plugin-publish") version "0.11.0"
    `maven-publish`
}

gradlePlugin {
    plugins {
        create("staticAnalysis") {
            id = "com.gradleup.static-analysis"
            displayName = "Static Analysis Gradle Plugin"
            description = "Easy and consistent setup of static analysis tools for Android and Java projects."
            implementationClass = "com.gradleup.staticanalysis.StaticAnalysisPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/GradleUp/static-analysis-plugin/"
    vcsUrl = "https://github.com/GradleUp/static-analysis-plugin.git"
    tags = listOf("pmd", "checkstyle", "spotbugs", "code-quality", "detekt", "ktlint", "lint", "android-lint", "GradleUp")
}

dependencies {
    testImplementation("junit:junit:4.13")
    testImplementation("com.google.truth:truth:0.30")
    testImplementation("com.google.guava:guava:19.0")
    testImplementation("org.mockito:mockito-core:2.13.0")
    testImplementation("com.google.code.findbugs:jsr305:3.0.0")
}
