package com.rabobank.rabobankassignment.report

import com.rabobank.rabobankassignment.model.InvalidRecord
import java.io.File

object ReportWriter {

    fun writeErrorReport(invalidRecords: List<InvalidRecord>, path: String = "errors.csv") {
        File(path).bufferedWriter().use { writer ->
            writer.write("reference,reason\n")
            invalidRecords.forEach {
                writer.write("${it.reference},${it.errors}\n")
            }
        }
    }
}
