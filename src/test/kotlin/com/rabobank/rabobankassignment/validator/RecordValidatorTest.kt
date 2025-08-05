package com.rabobank.rabobankassignment.validator

import com.rabobank.rabobankassignment.model.SwiftRecord
import com.rabobank.rabobankassignment.reader.SwiftRecordReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RecordValidatorTest {
    @Test
    fun shouldFilterAndValidateRecords() {
        val fakeReader = object : SwiftRecordReader {
            override fun readSwiftRecords(): Sequence<SwiftRecord> = sequenceOf(
                SwiftRecord("ref1", "account", "desc", 100.0, 50.0, 150.0), // valid
                SwiftRecord("ref1", "account", "desc", 100.0, 50.0, 140.0), // duplicate
                SwiftRecord("ref2", "account", "desc", 100.0, -30.0, 60.0)  // invalid balance
            )
        }

        val validator = RecordValidator(listOf(fakeReader))

        val records = validator.validateRecords()

        assertTrue(records.validRecords.isNotEmpty())
        assertTrue(records.invalidRecords.isNotEmpty())
        assertEquals("ref1", records.validRecords[0].reference)
        assertEquals("ref1", records.invalidRecords[0].reference)
    }
}
