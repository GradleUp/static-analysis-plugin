package com.gradleup.staticanalysis.internal.spotbugs

import groovy.util.slurpersupport.GPathResult

class SpotBugsViolationsEvaluator {

    private final GPathResult xml

    SpotBugsViolationsEvaluator(File report) {
        this(new XmlSlurper().parse(report))
    }

    SpotBugsViolationsEvaluator(GPathResult xml) {
        this.xml = xml
    }

    int errorsCount() {
        count('@priority_1')
    }

    private int count(String attr) {
        def count = xml.FindBugsSummary[attr]
        count == "" ? 0 : count.toInteger()
    }

    int warningsCount() {
        count("@priority_2") + count("@priority_3")
    }

}
