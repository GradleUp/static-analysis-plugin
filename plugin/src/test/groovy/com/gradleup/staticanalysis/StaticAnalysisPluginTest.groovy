package com.gradleup.staticanalysis

import com.gradleup.test.TestProject
import com.gradleup.test.TestProjectRule
import org.junit.Rule
import org.junit.Test

class StaticAnalysisPluginTest {

    @Rule
    public final TestProjectRule rule = new TestProjectRule({ new EmptyProject() }, { "" }, 'Empty project')

    @Test
    void shouldNotFailWhenNoJavaOrAndroidPluginsAreApplied() {
        rule.newProject()
                .build("help")
    }

    private static class EmptyProject extends TestProject<EmptyProject> {
        private static final Closure<String> TEMPLATE = { TestProject project ->
            """       
plugins {
    id("com.gradleup.static-analysis")
}
"""
        }

        EmptyProject() {
            super(TEMPLATE)
        }
    }
}
