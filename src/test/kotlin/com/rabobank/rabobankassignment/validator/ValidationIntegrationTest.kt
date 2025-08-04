package com.rabobank.rabobankassignment.validator

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class ValidationIntegrationTest @Autowired constructor(
    private val recordValidator: RecordValidator
) {

    @Test
    fun shouldReadAndValidateRecords() {
        val validRecords = recordValidator.validateRecords()

        assertTrue(validRecords.isNotEmpty())
        assertTrue(validRecords.none { it.reference == "183398" })
        assertTrue(validRecords.any { it.reference == "138932" })
    }
}