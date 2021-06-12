package com.gradleup.staticanalysis.internal.findbugs

import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CacheableTask
class GenerateFindBugsHtmlReport extends JavaExec {

    @InputFile
    @PathSensitive(RELATIVE)
    File xmlReportFile
    @OutputFile
    File htmlReportFile

    GenerateFindBugsHtmlReport() {
        onlyIf { xmlReportFile?.exists() }
        mainClass.set('edu.umd.cs.findbugs.PrintingBugReporter')
    }

    @Override
    void exec() {
        standardOutput = new FileOutputStream(htmlReportFile)
        args '-html', xmlReportFile
        super.exec()
    }
}
