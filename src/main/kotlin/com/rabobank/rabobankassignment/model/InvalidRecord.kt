package com.rabobank.rabobankassignment.model

data class InvalidRecord(val reference: String, val errors: Set<String>) {
}