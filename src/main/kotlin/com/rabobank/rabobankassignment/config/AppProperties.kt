package com.rabobank.rabobankassignment.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.input")
class AppProperties {
    lateinit var csvFile: String
    lateinit var xmlFile: String
}