package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.model.SwiftRecord

interface SwiftRecordReader {
    fun readSwiftRecords(): Sequence<SwiftRecord>
}