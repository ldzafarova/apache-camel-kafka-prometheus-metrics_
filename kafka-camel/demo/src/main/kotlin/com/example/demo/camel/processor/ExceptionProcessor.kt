package com.example.demo

import com.example.demo.infrastructure.config.ApplicationProperties
import com.example.demo.infrastructure.config.micrometer.CounterConfiguration.BeanNames.FAILED_CREATE_ACCOUNT_MESSAGE_COUNTER
import com.example.demo.infrastructure.config.micrometer.CounterConfiguration.BeanNames.FAILED_CREATE_TRANSACTION_ERROR_MESSAGE_COUNTER
import com.example.demo.infrastructure.config.micrometer.CounterConfiguration.BeanNames.FAILED_IMPORT_TRANSACTION_MESSAGE_COUNTER
import io.micrometer.core.instrument.Counter
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class ExceptionProcessor(
    private val counters: Map<String, Counter>,
    private val applicationProperties: ApplicationProperties
) : Processor {

    override fun process(exchange: Exchange) {
        val fromEndpoint = exchange.fromEndpoint.endpointBaseUri

        exchange.`in`.body = OriginalMessageWithException(
            originalMessage = exchange.`in`.body as Any,
            exceptionMessage = exchange.exception.message!!,
            fromEndpoint
        )

        incrementErrorCounter(fromEndpoint.replace("kafka://", ""))
    }

    private fun incrementErrorCounter(fromEndpoint: String) {
        val counterBeanName = when (fromEndpoint) {
            applicationProperties.kafka.topic.transactionErrorCreate -> FAILED_CREATE_TRANSACTION_ERROR_MESSAGE_COUNTER
            applicationProperties.kafka.topic.transactionImport -> FAILED_IMPORT_TRANSACTION_MESSAGE_COUNTER
            applicationProperties.kafka.topic.accountCreate -> FAILED_CREATE_ACCOUNT_MESSAGE_COUNTER
            else -> throw RuntimeException("Exchange fromEndpoint $fromEndpoint is not found")
        }

        counters.getValue(counterBeanName).increment()
    }
}

data class OriginalMessageWithException(
    var originalMessage: Any,
    var exceptionMessage: String,
    var fromEndpoint: String
)
