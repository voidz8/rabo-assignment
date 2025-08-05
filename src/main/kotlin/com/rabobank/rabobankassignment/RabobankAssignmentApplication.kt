package com.rabobank.rabobankassignment

import com.rabobank.rabobankassignment.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class) // TODO: this can be a @ConfigurationPropertiesScan
class RabobankAssignmentApplication

fun main(args: Array<String>) {
    runApplication<RabobankAssignmentApplication>(*args)
}
