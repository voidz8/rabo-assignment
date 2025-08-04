package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CsvReaderTest {


    @Test
    fun shouldParseRecordsFromCsVFile() {
        val props = AppProperties().apply { csvFile = "records.csv" }
        val reader = CsvReader(props)

        val records = reader.readSwiftRecords()

        assertTrue(records.isNotEmpty(), "CSV should contain records")
        assertEquals("183398", records[0].reference)
        assertEquals(2, records.size)
    }

    @Test
    fun shouldReadAndValidateInvalidCsvRecords() {
        val props = AppProperties().apply { csvFile = "invalid-records.csv" }
        val reader = CsvReader(props)

        val records = reader.readSwiftRecords()

        assertTrue(records.isEmpty(), "CSV should not contain records")
        assertEquals(0, records.size)
    }
}