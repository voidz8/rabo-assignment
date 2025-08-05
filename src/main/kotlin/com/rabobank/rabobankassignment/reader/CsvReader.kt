package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import com.rabobank.rabobankassignment.model.SwiftRecord
import org.apache.commons.csv.CSVFormat
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.InputStreamReader

@Component
class CsvReader(private val props: AppProperties) : SwiftRecordReader {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun readSwiftRecords(): Sequence<SwiftRecord> {
        val resource = ClassPathResource("/${props.csvFile}")
        logger.info("Reading Swift records from CSV")

        val inputStream = resource.inputStream
        val reader = InputStreamReader(inputStream)

        val format = CSVFormat.Builder.create()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setTrim(true)
            .build()

        return format.parse(reader)
            .asSequence()
            .mapNotNull { csvRecord ->
                try {
                    val reference = csvRecord.get("Reference")
                    val accountNumber = csvRecord.get("Account Number")
                    val description = csvRecord.get("Description")
                    val startBalance = csvRecord.get("Start Balance").toDoubleOrNull()
                    val mutation = csvRecord.get("Mutation").toDoubleOrNull()
                    val endBalance = csvRecord.get("End Balance").toDoubleOrNull()

                    if (reference.isNullOrBlank() || accountNumber.isNullOrBlank() ||
                        startBalance == null || mutation == null || endBalance == null
                    ) {
                        logger.warn("Skipping malformed CSV record: $csvRecord")
                        null
                    } else {
                        SwiftRecord(
                            reference,
                            accountNumber,
                            description,
                            startBalance,
                            mutation,
                            endBalance
                        )
                    }
                } catch (e: Exception) {
                    logger.warn("Skipping invalid CSV record due to error: $csvRecord", e)
                    null
                }
            }
    }
}
