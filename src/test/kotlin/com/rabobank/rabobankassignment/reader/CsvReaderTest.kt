package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CsvReaderTest {

    @Test
    fun shouldParseRecordsFromCsVFile() {
        val props = AppProperties("records.csv", "records.xml")
        val reader = CsvReader(props)

        val records = reader.readSwiftRecords().toList()

        assertTrue(records.isNotEmpty())
        assertEquals("183398", records[0].reference)
        assertEquals(3, records.size)
    }

    @Test
    fun shouldReadAndValidateInvalidCsvRecords() {
        val props = AppProperties("invalid-records.csv", "invalid-records.xml")
        val reader = CsvReader(props)

        val records = reader.readSwiftRecords().toList()

        assertTrue(records.isEmpty())
        assertEquals(0, records.size)
    }

    @Test
    fun shouldReadAndValidateEmptyCsv() {
        val props = AppProperties("empty.csv", "empty.xml")
        val reader = CsvReader(props)

        val records = reader.readSwiftRecords().toList()

        assertTrue(records.isEmpty())
        assertEquals(0, records.size)
    }
}
