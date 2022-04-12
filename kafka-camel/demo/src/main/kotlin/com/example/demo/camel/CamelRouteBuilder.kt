package com.example.demo.camel

import com.example.demo.infrastructure.config.ApplicationProperties
import com.example.demo.CreateTransactionErrorCommand
import com.example.demo.ExceptionProcessor
import com.example.demo.TransactionDto
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.jackson.JacksonDataFormat
import org.springframework.stereotype.Component

@Component
class CamelRouteBuilder(
    private val applicationProperties: ApplicationProperties,
    private val objectMapper: ObjectMapper,
    private val exceptionProcessor: ExceptionProcessor
): RouteBuilder() {
    override fun configure() {
        context.isAllowUseOriginalMessage = true
        initCreateTransactionErrorRoute()
        initCreateAccountRoute()
        initImportTransaction()
    }

    private fun initCreateTransactionErrorRoute() {
        initRoute<CreateTransactionErrorCommand>(
            fromRoute = applicationProperties.camel.transactionErrorCreateRoute,
            messageType = "Create Transaction Error",
            processorName = "createTransactionErrorProcessor"
        )
    }

    private fun initCreateAccountRoute() {
        initRoute<TransactionDto>(
            fromRoute = applicationProperties.camel.accountCreateRoute,
            messageType = "Create Account",
            processorName = "createAccountProcessor"
        )
    }

    private fun initImportTransaction() {
        initRoute<TransactionDto>(
            fromRoute = applicationProperties.camel.transactionImportRoute,
            messageType = "Import Transaction",
            processorName = "importTransactionProcessor"
        )
    }

    private inline fun <reified T> initRoute(fromRoute: String, messageType: String, processorName: String) {
        val dataFormat = JacksonDataFormat(T::class.java)
        dataFormat.objectMapper = objectMapper

        from(fromRoute)
            .onException(JacksonException::class.java)
            .handled(true)
            .log(LoggingLevel.ERROR, "JacksonException in processing $messageType message:  \${exception.message}")
            .onExceptionOccurred(exceptionProcessor)
            .logHandled(true)
            .to(applicationProperties.camel.deadLetterChannelRoute)
            .end()
            .onException(Exception::class.java)
            .handled(true)
            .log(LoggingLevel.ERROR, "Exception in processing $messageType message:  \${exception.message}")
            .onExceptionOccurred(exceptionProcessor)
            .useCollisionAvoidance()
            .useExponentialBackOff()
            .maximumRedeliveries(3)
            .redeliveryDelay(REDELIVERY_DELAY)
            .logHandled(true)
            .logRetryStackTrace(true)
            .to(applicationProperties.camel.deadLetterChannelRoute)
            .end()
            .log(LoggingLevel.INFO, "Processing $messageType message: \${body}")
            .unmarshal(dataFormat)
            .process(processorName)
            .log("$messageType message processed successfully")
    }

    companion object {
        const val REDELIVERY_DELAY = 10 * 1000L
    }
}