package com.rabobank.rabobankassignment.validator

import com.rabobank.rabobankassignment.report.ReportWriter
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class ValidationRunner(private val recordValidator: RecordValidator, private val reportWriter: ReportWriter) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        logger.info("Starting record validation.")
        val records = recordValidator.validateRecords()
        logger.info("Validation finished. Found ${records.validRecords} valid records.")
        reportWriter.writeErrorReport(records.invalidRecords)
        logger.info("Error report written to errors.csv.")
    }
}