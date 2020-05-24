package com.gradleup.staticanalysis

import com.gradleup.staticanalysis.internal.Configurator
import com.gradleup.staticanalysis.internal.checkstyle.CheckstyleConfigurator
import com.gradleup.staticanalysis.internal.detekt.DetektConfigurator
import com.gradleup.staticanalysis.internal.ktlint.KtlintConfigurator
import com.gradleup.staticanalysis.internal.lint.LintConfigurator
import com.gradleup.staticanalysis.internal.pmd.PmdConfigurator
import com.gradleup.staticanalysis.internal.spotbugs.SpotBugsConfigurator
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import static com.gradleup.staticanalysis.internal.TasksCompat.configureEach

class StaticAnalysisPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def pluginExtension = project.extensions.create('staticAnalysis', StaticAnalysisExtension, project)
        def evaluateViolations = createEvaluateViolationsTask(project, pluginExtension)
        createConfigurators(project, pluginExtension, evaluateViolations).each { configurator -> configurator.execute() }
        configureEach(project.tasks.matching { it.name == 'check' }) { task ->
            task.dependsOn evaluateViolations
        }
    }

    private static Task createEvaluateViolationsTask(Project project,
                                                     StaticAnalysisExtension extension) {
        project.tasks.create('evaluateViolations', EvaluateViolationsTask) { task ->
            task.evaluator = { extension.evaluator }
            task.allViolations = { extension.allViolations }
        }
    }

    private static List<Configurator> createConfigurators(Project project,
                                                          StaticAnalysisExtension pluginExtension,
                                                          Task evaluateViolations) {
        NamedDomainObjectContainer<Violations> violationsContainer = pluginExtension.allViolations
        [
                CheckstyleConfigurator.create(project, violationsContainer, evaluateViolations),
                PmdConfigurator.create(project, violationsContainer, evaluateViolations),
                SpotBugsConfigurator.create(project, violationsContainer, evaluateViolations),
                DetektConfigurator.create(project, violationsContainer, evaluateViolations),
                KtlintConfigurator.create(project, violationsContainer, evaluateViolations),
                LintConfigurator.create(project, violationsContainer, evaluateViolations)
        ]
    }
}
