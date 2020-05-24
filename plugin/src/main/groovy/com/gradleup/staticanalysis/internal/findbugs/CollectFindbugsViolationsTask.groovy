package com.gradleup.staticanalysis.internal.findbugs

import com.gradleup.staticanalysis.Violations
import com.gradleup.staticanalysis.internal.CollectViolationsTask

class CollectFindbugsViolationsTask extends CollectViolationsTask {

    @Override
    void collectViolations(File xmlReportFile, File htmlReportFile, Violations violations) {
        def evaluator = new FinbugsViolationsEvaluator(xmlReportFile)
        violations.addViolations(evaluator.errorsCount(), evaluator.warningsCount(), htmlReportFile)
    }
}
