package com.gradleup.staticanalysis.internal.checkstyle

import com.gradleup.staticanalysis.Violations
import com.gradleup.staticanalysis.internal.CollectViolationsTask
import groovy.util.slurpersupport.GPathResult

class CollectCheckstyleViolationsTask extends CollectViolationsTask {

    @Override
    void collectViolations(File xmlReportFile, File htmlReportFile, Violations violations) {
        GPathResult xml = new XmlSlurper().parse(xmlReportFile)
        int errors = xml.'**'.findAll { node -> node.name() == 'error' && node.@severity == 'error' }.size()
        int warnings = xml.'**'.findAll { node -> node.name() == 'error' && node.@severity == 'warning' }.size()
        violations.addViolations(errors, warnings, htmlReportFile ?: xmlReportFile)
    }

}
