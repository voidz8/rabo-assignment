package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class XmlReaderTest {

    @Test
    fun shouldParseRecordsFromXmlFile() {
        val props = AppProperties("records.csv", "records.xml")
        val reader = XmlReader(props)

        val records = reader.readSwiftRecords().toList()

        assertTrue(records.isNotEmpty())
        assertEquals("138932", records[0].reference)
        assertEquals(3, records.size)
    }

    @Test
    fun shouldReadAndValidateInvalidXmlRecords() {
        val props = AppProperties("invalid-records.csv", "invalid-records.xml")
        val reader = XmlReader(props)

        val records = reader.readSwiftRecords().toList()

        assertTrue(records.isNotEmpty())
        assertEquals(1, records.size)
        assertEquals("138932", records[0].reference)
    }

    @Test
    fun shouldReadAndValidateEmptyXml() {
        val props = AppProperties("empty.csv", "empty.xml")
        val reader = XmlReader(props)

        val records = reader.readSwiftRecords().toList()

        assertTrue(records.isEmpty())
    }
}
