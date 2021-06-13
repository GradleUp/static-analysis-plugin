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
            displayName = findProperty("POM_NAME")?.toString()
            description = findProperty("POM_DESCRIPTION")?.toString()
            implementationClass = "com.gradleup.staticanalysis.StaticAnalysisPlugin"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
    testImplementation("org.mockito:mockito-core:3.11.1")
    testImplementation("com.google.code.findbugs:jsr305:3.0.0")
}

publishing {
    publications.withType<MavenPublication> {
        artifactId = findProperty("POM_ARTIFACT_ID")?.toString()
        pom {
            name.set(findProperty("POM_NAME")?.toString())
            description.set(findProperty("POM_DESCRIPTION")?.toString())
            packaging = findProperty("POM_PACKAGING")?.toString()
        }
    }
}

