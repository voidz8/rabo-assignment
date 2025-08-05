package com.rabobank.rabobankassignment.report

import com.rabobank.rabobankassignment.model.InvalidRecord
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class ReportWriterTest {

    @Test
    fun shouldWriteInvalidRecordsToCSV(@TempDir tempDir: Path) {
        val testFile = tempDir.resolve("errors.csv").toFile()

        val invalidRecords = listOf(
            InvalidRecord("12345", setOf("Duplicate reference")),
            InvalidRecord("67890", setOf("End balance incorrect", "Duplicate reference"))
        )

        ReportWriter.writeErrorReport(invalidRecords, testFile.absolutePath)

        val lines = testFile.readLines()
        assertEquals(3, lines.size)
        assertEquals("reference,reason", lines[0])
        assertEquals("12345,[Duplicate reference]", lines[1])
        assertEquals("67890,[End balance incorrect, Duplicate reference]", lines[2])
    }
}
