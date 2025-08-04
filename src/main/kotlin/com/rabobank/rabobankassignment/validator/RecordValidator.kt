package com.rabobank.rabobankassignment.validator

import com.rabobank.rabobankassignment.model.SwiftRecord
import com.rabobank.rabobankassignment.reader.SwiftRecordReader
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RecordValidator(private val recordReaders: List<SwiftRecordReader>) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun validateRecords(): List<SwiftRecord> {
        val swiftRecords = recordReaders.flatMap { reader: SwiftRecordReader -> reader.readSwiftRecords() }
        val checkedReferences = mutableSetOf<String>()

        return swiftRecords.filter { record ->
            val isDuplicate = !checkedReferences.add(record.reference)
            val isEndBalanceIncorrect = record.endBalance != record.startBalance + record.mutation

            if (isDuplicate) {
                logger.warn("Duplicate reference found: ${record.reference}")
            }
            if (isEndBalanceIncorrect) {
                logger.warn("Incorrect mutation found for record: ${record.reference}")
            }

            !isDuplicate && !isEndBalanceIncorrect
        }
    }
}