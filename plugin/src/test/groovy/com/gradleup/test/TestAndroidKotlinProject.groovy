package com.gradleup.test

class TestAndroidKotlinProject extends TestProject<TestAndroidKotlinProject> {
    private static final Closure<String> TEMPLATE = { TestAndroidKotlinProject project ->
        """
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:4.2.1'
        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21'
    }
}
plugins {
    ${formatPlugins(project)}
    id 'com.gradleup.static-analysis'   
}
repositories {
    google()
    jcenter()
}
apply plugin: 'com.android.library'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt' // adding kapt since we face compat issues before

android {
    compileSdkVersion 29

    defaultConfig {
        minSdkVersion 16
        targetSdkVersion 29
        versionCode 1
        versionName '1.0'
    }
    lintOptions {
        disable 'OldTargetApi'
    }
    sourceSets {
        ${formatSourceSets(project)}
    }
    ${project.additionalAndroidConfig}
}         

dependencies {
    implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.3.21'
}

${formatExtension(project)}
"""
    }

    private String additionalAndroidConfig = ''

    TestAndroidKotlinProject() {
        super(TEMPLATE)
        File localProperties = Fixtures.LOCAL_PROPERTIES
        if (localProperties.exists()) {
            withFile(localProperties, 'local.properties')
        }
    }

    private static String formatSourceSets(TestProject project) {
        project.sourceSets
                .entrySet()
                .collect { Map.Entry<String, List<String>> entry ->
            """$entry.key {
            manifest.srcFile '${Fixtures.ANDROID_MANIFEST}'
            java {
                ${entry.value.collect { "srcDir '$it'" }.join('\n\t\t\t\t')}
            }
        }"""
        }
        .join('\n\t\t')
    }

    @Override
    List<String> defaultArguments() {
        ['-x', 'lint'] + super.defaultArguments()
    }

    TestAndroidKotlinProject withAdditionalAndroidConfig(String additionalAndroidConfig) {
        this.additionalAndroidConfig = additionalAndroidConfig
        return this
    }
}
