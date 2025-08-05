package com.rabobank.rabobankassignment.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "app.input")
class AppProperties { //@TODO make this a data class so it will be immutable and the properties can be set at construction time not runtime.
    lateinit var csvFile: String
    lateinit var xmlFile: String
}
