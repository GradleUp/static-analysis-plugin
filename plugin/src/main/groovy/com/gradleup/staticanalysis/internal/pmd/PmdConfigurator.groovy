package com.gradleup.staticanalysis.internal.pmd

import com.gradleup.staticanalysis.Violations
import com.gradleup.staticanalysis.internal.CodeQualityConfigurator
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.PmdExtension

class PmdConfigurator extends CodeQualityConfigurator<Pmd, PmdExtension> {

    static PmdConfigurator create(Project project,
                                  NamedDomainObjectContainer<Violations> violationsContainer,
                                  Task evaluateViolations) {
        Violations violations = violationsContainer.maybeCreate('PMD')
        return new PmdConfigurator(project, violations, evaluateViolations)
    }

    private PmdConfigurator(Project project,
                            Violations violations,
                            Task evaluateViolations) {
        super(project, violations, evaluateViolations)
    }

    @Override
    protected String getToolName() {
        'pmd'
    }

    @Override
    protected Class<PmdExtension> getExtensionClass() {
        PmdExtension
    }

    @Override
    protected Class<Pmd> getTaskClass() {
        Pmd
    }

    @Override
    protected void createToolTaskForAndroid(sourceSet) {
        project.tasks.register(getToolTaskNameFor(sourceSet), Pmd) { Pmd task ->
            task.description = "Run PMD analysis for sourceSet ${sourceSet.name} classes"
            task.source = sourceSet.java.srcDirs
        }
    }

    @Override
    protected def createCollectViolations(String taskName, Violations violations) {
        project.tasks.register("collect${taskName.capitalize()}Violations", CollectPmdViolationsTask) { task ->
            def pmd = project.tasks[taskName] as Pmd
            task.xmlReportFile = pmd.reports.xml.destination
            task.violations = violations
            task.dependsOn(pmd)
        }
    }
}
