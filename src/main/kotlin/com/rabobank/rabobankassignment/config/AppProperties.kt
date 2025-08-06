package com.rabobank.rabobankassignment.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.input")
data class AppProperties (val csvFile: String, val xmlFile: String)