package com.rabobank.rabobankassignment.validator

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class ValidationRunner(private val recordValidator: RecordValidator) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(ValidationRunner::class.java)

    override fun run(vararg args: String?) {
        logger.info("Starting record validation.")
        val validRecords = recordValidator.validateRecords()
        logger.info("Validation finished. Found ${validRecords.size} valid records.")
    }
}