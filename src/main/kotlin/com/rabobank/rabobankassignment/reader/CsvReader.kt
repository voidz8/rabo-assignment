package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.model.SwiftRecord
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class CsvReader : SwiftRecordReader {

    private val logger = LoggerFactory.getLogger(CsvReader::class.java)

    override fun readSwiftRecords(): List<SwiftRecord> {
        val resource = ClassPathResource("/records.csv")
        logger.info("Reading Swift records from CSV...")

        return resource.inputStream.bufferedReader().useLines { lines ->
            lines.drop(1)
                .map { line ->
                    val columns = line.split(",")
                    SwiftRecord(
                        reference = columns[0],
                        accountNumber = columns[1],
                        description = columns[2],
                        startBalance = columns[3].toDouble(),
                        mutation = columns[4].toDouble(),
                        endBalance = columns[5].toDouble())
                }.toList()
        }
    }
}