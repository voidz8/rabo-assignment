package com.rabobank.rabobankassignment.reader

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class XmlReaderTest {
    private val reader = XmlReader()

    @Test
    fun `should parse records from XML file`() {
        val records = reader.readSwiftRecords()

        assertTrue(records.isNotEmpty())
        assertEquals("138932", records[0].reference)
        assertEquals(2, records.size)
    }
}