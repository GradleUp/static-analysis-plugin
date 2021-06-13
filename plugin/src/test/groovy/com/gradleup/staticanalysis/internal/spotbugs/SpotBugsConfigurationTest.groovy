package com.gradleup.staticanalysis.internal.spotbugs

import com.gradleup.test.TestProjectRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import static com.gradleup.test.Fixtures.Findbugs.SOURCES_WITH_LOW_VIOLATION

@RunWith(Parameterized.class)
class SpotBugsConfigurationTest {

    @Parameterized.Parameters(name = "{0}")
    static Iterable<TestProjectRule> rules() {
        return [
                TestProjectRule.forJavaProject(),
                TestProjectRule.forKotlinProject(),
                // TODO Android tests are currently disabled because of ClassNotFound
                // TestProjectRule.forAndroidProject(),
                // TestProjectRule.forAndroidKotlinProject(),
        ]
    }

    @Rule
    public final TestProjectRule projectRule

    SpotBugsConfigurationTest(TestProjectRule projectRule) {
        this.projectRule = projectRule
    }

    @Test
    void shouldConfigureSuccessFully() {
        projectRule.newProject()
                .withPlugin('com.github.spotbugs', "4.7.1")
                .withSourceSet('main', SOURCES_WITH_LOW_VIOLATION)
                .withToolsConfig("spotbugs { }")
                .build('check', '--dry-run')
    }

    @Test
    void shouldNotFailBuildWhenSpotBugsIsConfiguredMultipleTimes() {
        projectRule.newProject()
                .withPlugin('com.github.spotbugs', "4.7.1")
                .withSourceSet('main', SOURCES_WITH_LOW_VIOLATION)
                .withPenalty('none')
                .withToolsConfig("""
                    spotbugs { }
                    spotbugs {
                        effort = "max"
                    }
                """)
                .build('check', '--dry-run')
    }
}
