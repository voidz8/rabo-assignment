package com.rabobank.rabobankassignment.validator

import com.rabobank.rabobankassignment.model.InvalidRecord
import com.rabobank.rabobankassignment.model.SwiftRecord
import com.rabobank.rabobankassignment.model.ValidationResult
import com.rabobank.rabobankassignment.reader.SwiftRecordReader
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class RecordValidator(private val recordReaders: List<SwiftRecordReader>) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun validateRecords(): ValidationResult {
        val allRecords = recordReaders.flatMap { it.readSwiftRecords() } // TODO: What if the files are very large? Should we not stream them instead?
        val seenReferences = mutableSetOf<String>()

        val validRecords = mutableListOf<SwiftRecord>()
        val invalidRecords = mutableListOf<InvalidRecord>()

        for (record in allRecords) {
            val errors = mutableSetOf<String>()

            if (!seenReferences.add(record.reference)) {
                errors.add("Duplicate reference")
            }

            if (abs(record.endBalance - (record.startBalance + record.mutation)) > 0.01) {
                errors.add("End balance incorrect")
            }

            if (errors.isEmpty()) {
                validRecords.add(record)
            } else {
                logger.warn("Invalid record: reference=${record.reference}, reasons=$errors")
                invalidRecords.add(InvalidRecord(record.reference, errors))
            }
        }

        return ValidationResult(validRecords, invalidRecords)
    }

}
