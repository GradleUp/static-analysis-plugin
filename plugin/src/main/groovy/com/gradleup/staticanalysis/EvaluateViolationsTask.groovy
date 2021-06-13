package com.gradleup.staticanalysis

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class EvaluateViolationsTask extends DefaultTask {

    @Input
    Closure<ViolationsEvaluator> evaluator
    @Input
    Closure<Set<Violations>> allViolations

    EvaluateViolationsTask() {
        group = 'verification'
        description = 'Evaluate total violations against penaltyExtension thresholds.'
    }


    @TaskAction
    void run() {
        evaluator().evaluate(allViolations())
    }
}
