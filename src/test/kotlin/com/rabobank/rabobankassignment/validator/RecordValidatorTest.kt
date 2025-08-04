package com.rabobank.rabobankassignment.validator

import com.rabobank.rabobankassignment.model.SwiftRecord
import com.rabobank.rabobankassignment.reader.SwiftRecordReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RecordValidatorTest {


    @Test
    fun shouldFilterAndValidateRecords() {
        val fakeReader = object : SwiftRecordReader {
            override fun readSwiftRecords(): List<SwiftRecord> = listOf(
                SwiftRecord("ref1", "account", "desc", 100.0, 50.0, 150.0), // valid
                SwiftRecord("ref1", "account", "desc", 100.0, 50.0, 140.0), // duplicate
                SwiftRecord("ref2", "account", "desc", 100.0, -30.0, 60.0)  // invalid balance
            )
        }

        val validator = RecordValidator(listOf(fakeReader))

        val validRecords = validator.validateRecords()

        assertEquals(1, validRecords.size)
        assertEquals("ref1", validRecords[0].reference)
    }
}