package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import com.rabobank.rabobankassignment.model.SwiftRecord
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class CsvReader(private val props: AppProperties) : SwiftRecordReader {

    private val logger = LoggerFactory.getLogger(CsvReader::class.java)

    override fun readSwiftRecords(): List<SwiftRecord> {
        val resource = ClassPathResource("/${props.csvFile}")
        logger.info("Reading Swift records from CSV...")

        return resource.inputStream.bufferedReader().useLines { lines ->
            lines.drop(1) // drop the headers
                .mapNotNull { line ->
                    val columns = line.split(",")

                    if (columns.size < 6 || listOf(columns[0], columns[1], columns[3], columns[4], columns[5]).any { it.isBlank() }) {
                        logger.warn("Skipping malformed line $line")
                        return@mapNotNull null
                    }

                    try {
                        SwiftRecord(
                            reference = columns[0],
                            accountNumber = columns[1],
                            description = columns[2],
                            startBalance = columns[3].toDouble(),
                            mutation = columns[4].toDouble(),
                            endBalance = columns[5].toDouble()
                        )
                    } catch (e: NumberFormatException) {
                        logger.warn("Skipping malformed line (invalid number): $line", e)
                        null
                    }
                }.toList()
        }
    }
}
