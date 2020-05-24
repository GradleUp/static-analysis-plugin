package com.gradleup.test

final class TestKotlinProject extends TestProject<TestKotlinProject> {

    private static final Closure<String> TEMPLATE = { TestProject project ->
        """
buildscript {
    repositories { 
        jcenter()
    }
    dependencies {
        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21'
    }
}

plugins {
    ${formatPlugins(project)}
    id 'com.gradleup.static-analysis'
}

apply plugin: 'kotlin'
apply plugin: 'kotlin-kapt' // adding kapt since we face compat issues before

repositories { 
    jcenter()
}

dependencies {
    implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.3.21'
}

sourceSets {
    ${formatSourceSets(project)}
}
${formatExtension(project)}
"""
    }

    TestKotlinProject() {
        super(TEMPLATE)
    }

    private static String formatSourceSets(TestProject project) {
        project.sourceSets
                .entrySet()
                .collect { Map.Entry<String, List<String>> entry ->
            """$entry.key {
        kotlin {
            ${entry.value.collect { "srcDir '$it'" }.join('\n\t\t\t\t')}
        }
    }"""
        }
        .join('\n\t')
    }
}
