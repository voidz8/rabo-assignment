package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import com.rabobank.rabobankassignment.model.SwiftRecord
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class CsvReader(private val props: AppProperties) : SwiftRecordReader { // TODO: Why is this bean a service and not a component?

    private val logger = LoggerFactory.getLogger(this::class.java)

    //TODO add javadoc
    override fun readSwiftRecords(): List<SwiftRecord> {
        val resource = ClassPathResource("/${props.csvFile}")
        logger.info("Reading Swift records from CSV...")

        return resource.inputStream.bufferedReader().useLines { lines ->
            lines.drop(1) // drop the headers
                .mapNotNull { line ->
                    val columns = line.split(",") // TODO: Should we use a CSV parser library instead to handle edge cases like commas in fields, quoted strings, etc.? Jackson CSV, Apache Commons CSV, etc.?

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
                }.toList() // TODO: Should we use a sequence instead to avoid loading everything into memory at once?
        }
    }
}
