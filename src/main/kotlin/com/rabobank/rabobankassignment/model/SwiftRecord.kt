package com.rabobank.rabobankassignment.model

data class SwiftRecord(
    val reference: String,
    val accountNumber: String,
    val description: String,
    val startBalance: Double,
    val mutation: Double,
    val endBalance: Double
)