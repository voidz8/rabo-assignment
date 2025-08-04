package com.rabobank.rabobankassignment.reader

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CsvReaderTest {
    private val reader = CsvReader()

    @Test
    fun shouldParseRecordsFromCsVFile() {
        val records = reader.readSwiftRecords()

        assertTrue(records.isNotEmpty(), "CSV should contain records")
        assertEquals("183398", records[0].reference)
        assertEquals(2, records.size)
    }
}