package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class XmlReaderTest {
    private val props = AppProperties().apply { xmlFile = "records.xml" }
    private val reader = XmlReader(props)

    @Test
    fun shouldParseRecordsFromXmlFile() {
        val records = reader.readSwiftRecords()

        assertTrue(records.isNotEmpty())
        assertEquals("138932", records[0].reference)
        assertEquals(2, records.size)
    }

    @Test
    fun shouldReadAndValidateInvalidXmlRecords() {
        val props = AppProperties().apply { xmlFile = "invalid-records.xml" }
        val reader = XmlReader(props)

        val records = reader.readSwiftRecords()

        assertTrue(records.isNotEmpty())
        assertEquals(1, records.size)
        assertEquals("138932", records[0].reference)
    }
    @Test
    fun shouldReadAndValidateEmptyXml() {
        val props = AppProperties().apply { xmlFile = "empty.xml" }
        val reader = XmlReader(props)

        val records = reader.readSwiftRecords()

        assertTrue(records.isEmpty())
    }
}