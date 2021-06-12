package com.gradleup.staticanalysis.internal.pmd

import com.gradleup.test.Fixtures
import com.gradleup.test.TestProject
import com.gradleup.test.TestProjectRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import static com.gradleup.test.LogsSubject.assertThat

@RunWith(Parameterized.class)
public class PmdIntegrationTest {

    private static final String DEFAULT_RULES = "project.files('${Fixtures.Pmd.RULES.path}')"

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<TestProjectRule> rules() {
        return [TestProjectRule.forJavaProject(), TestProjectRule.forAndroidProject()]
    }

    @Rule
    public final TestProjectRule projectRule

    public PmdIntegrationTest(TestProjectRule projectRule) {
        this.projectRule = projectRule
    }

    @Test
    public void shouldFailBuildWhenPmdWarningsOverTheThreshold() {
        TestProject.Result result = projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_3_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 0
                    maxErrors = 0
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES))
                .buildAndFail('check')

        assertThat(result.logs).containsLimitExceeded(0, 1)
        assertThat(result.logs).containsPmdViolations(0, 1,
                result.buildFileUrl('reports/pmd/main.html'))
    }

    @Test
    public void shouldFailBuildAfterSecondRunWhenPmdWarningsStillOverTheThreshold() {
        def project = projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_3_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 0
                    maxErrors = 0
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES))

        TestProject.Result result = project.buildAndFail('check')

        assertThat(result.logs).containsLimitExceeded(0, 1)
        assertThat(result.logs).containsPmdViolations(0, 1,
                result.buildFileUrl('reports/pmd/main.html'))

        result = project.buildAndFail('check')

        assertThat(result.logs).containsLimitExceeded(0, 1)
        assertThat(result.logs).containsPmdViolations(0, 1,
                result.buildFileUrl('reports/pmd/main.html'))
    }

    @Test
    public void shouldFailBuildWhenPmdErrorOverTheThreshold() {
        TestProject.Result result = projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_1_VIOLATION, Fixtures.Pmd.SOURCES_WITH_PRIORITY_2_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 0
                    maxErrors = 0
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES))
                .buildAndFail('check')

        assertThat(result.logs).containsLimitExceeded(2, 1)
        assertThat(result.logs).containsPmdViolations(2, 1,
                result.buildFileUrl('reports/pmd/main.html'))
    }

    @Test
    public void shouldNotFailBuildWhenNoPmdWarningsOrErrorsEncounteredAndNoThresholdTrespassed() {
        TestProject.Result result = projectRule.newProject()
                .withPenalty('''{
                    maxWarnings = 0
                    maxErrors = 0
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES))
                .build('check')

        assertThat(result.logs).doesNotContainLimitExceeded()
        assertThat(result.logs).doesNotContainPmdViolations()
    }

    @Test
    public void shouldNotFailBuildWhenPmdWarningsAndErrorsEncounteredAndNoThresholdTrespassed() {
        TestProject.Result result = projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_1_VIOLATION, Fixtures.Pmd.SOURCES_WITH_PRIORITY_2_VIOLATION)
                .withSourceSet('test', Fixtures.Pmd.SOURCES_WITH_PRIORITY_3_VIOLATION, Fixtures.Pmd.SOURCES_WITH_PRIORITY_4_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 100
                    maxErrors = 100
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES))
                .build('check')

        assertThat(result.logs).doesNotContainLimitExceeded()
        assertThat(result.logs).containsPmdViolations(2, 3,
                result.buildFileUrl('reports/pmd/main.html'),
                result.buildFileUrl('reports/pmd/test.html'))
    }

    @Test
    public void shouldNotFailBuildWhenPmdConfiguredToNotIgnoreFailures() {
        projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_1_VIOLATION, Fixtures.Pmd.SOURCES_WITH_PRIORITY_2_VIOLATION)
                .withSourceSet('test', Fixtures.Pmd.SOURCES_WITH_PRIORITY_3_VIOLATION, Fixtures.Pmd.SOURCES_WITH_PRIORITY_4_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 100
                    maxErrors = 100
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES, "ignoreFailures = false"))
                .build('check')
    }

    @Test
    public void shouldNotFailBuildWhenPmdConfiguredToExcludePatterns() {
        TestProject.Result result = projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_1_VIOLATION)
                .withSourceSet('test', Fixtures.Pmd.SOURCES_WITH_PRIORITY_2_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 0
                    maxErrors = 0
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES, "exclude 'Priority1Violator.java'", "exclude 'Priority2Violator.java'"))
                .build('check')

        assertThat(result.logs).doesNotContainLimitExceeded()
        assertThat(result.logs).doesNotContainPmdViolations()
    }

    @Test
    public void shouldNotFailBuildWhenPmdConfiguredToIgnoreFaultySourceFolders() {
        TestProject.Result result = projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_1_VIOLATION)
                .withSourceSet('test', Fixtures.Pmd.SOURCES_WITH_PRIORITY_2_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 0
                    maxErrors = 0
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES,
                "exclude project.fileTree('${Fixtures.Pmd.SOURCES_WITH_PRIORITY_1_VIOLATION}')",
                "exclude project.fileTree('${Fixtures.Pmd.SOURCES_WITH_PRIORITY_2_VIOLATION}')"))
                .build('check')

        assertThat(result.logs).doesNotContainLimitExceeded()
        assertThat(result.logs).doesNotContainPmdViolations()
    }

    @Test
    public void shouldNotFailBuildWhenPmdConfiguredToIgnoreFaultySourceSets() {
        TestProject.Result result = projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_1_VIOLATION)
                .withSourceSet('test', Fixtures.Pmd.SOURCES_WITH_PRIORITY_2_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 0
                    maxErrors = 0
                }''')
                .withToolsConfig(pmd(DEFAULT_RULES,
                "exclude ${projectRule.printSourceSet('main')}.java.srcDirs",
                "exclude ${projectRule.printSourceSet('test')}.java.srcDirs"))
                .build('check')

        assertThat(result.logs).doesNotContainLimitExceeded()
        assertThat(result.logs).doesNotContainPmdViolations()
    }

    @Test
    public void shouldNotFailBuildWhenPmdNotConfigured() {
        TestProject.Result result = projectRule.newProject()
                .withSourceSet('main', Fixtures.Pmd.SOURCES_WITH_PRIORITY_1_VIOLATION)
                .withSourceSet('test', Fixtures.Pmd.SOURCES_WITH_PRIORITY_2_VIOLATION)
                .withPenalty('''{
                    maxWarnings = 0
                    maxErrors = 0
                }''')
                .build('check')

        assertThat(result.logs).doesNotContainLimitExceeded()
        assertThat(result.logs).doesNotContainPmdViolations()
    }

    private String pmd(String rules, String... configs) {
        """pmd {
            ruleSetFiles = $rules
            ${configs.join('\n\t\t\t')}
        }"""
    }
}
