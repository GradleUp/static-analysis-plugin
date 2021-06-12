package com.gradleup.staticanalysis.internal.spotbugs

import com.gradleup.staticanalysis.Violations
import com.gradleup.staticanalysis.internal.CollectViolationsTask

class CollectSpotBugsViolationsTask extends CollectViolationsTask {

    @Override
    void collectViolations(File xmlReportFile, File htmlReportFile, Violations violations) {
        def evaluator = new SpotBugsViolationsEvaluator(xmlReportFile)
        violations.addViolations(evaluator.errorsCount(), evaluator.warningsCount(), htmlReportFile)
    }
}
