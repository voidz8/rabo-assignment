package com.rabobank.rabobankassignment.model

data class ValidationResult(val validRecords: List<SwiftRecord>, val invalidRecords: List<InvalidRecord>)
