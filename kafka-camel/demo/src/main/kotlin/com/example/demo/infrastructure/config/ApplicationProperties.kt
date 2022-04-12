package com.example.demo.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "application")
class ApplicationProperties(
    val camel: Camel,
    val kafka: Kafka
) {
    data class Camel(
        val deadLetterChannelRoute: String,
        val transactionErrorCreateRoute: String,
        val transactionImportRoute: String,
        val accountCreateRoute: String
    )

    data class Kafka(val topic: Topic) {

        data class Topic(
            val transactionErrorCreate: String,
            val transactionImport: String,
            val accountCreate: String,
            val deadLetterChannel: String
        )
    }
}