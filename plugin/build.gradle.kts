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

publishing {
    publications.withType<MavenPublication> {
        pom {
            groupId = findProperty("GROUP")?.toString()
            version = findProperty("VERSION_NAME")?.toString()

            name.set(findProperty("POM_NAME")?.toString())
            description.set(findProperty("POM_DESCRIPTION")?.toString())
            packaging = findProperty("POM_PACKAGING")?.toString()
            url.set(findProperty("POM_URL")?.toString())

            scm {
                url.set(findProperty("POM_SCM_URL")?.toString())
                connection.set(findProperty("POM_SCM_CONNECTION")?.toString())
            }

            licenses {
                license {
                    name.set(findProperty("POM_LICENCE_NAME")?.toString())
                    url.set(findProperty("POM_LICENCE_URL")?.toString())
                }
            }
        }
    }

    repositories {
        maven {
            name = "bintray"
            url = uri("https://api.bintray.com/maven/gradleup/maven/static-analysis-plugin/;publish=1;override=1")
            credentials {
                username = System.getenv("BINTRAY_USER")
                password = System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}

