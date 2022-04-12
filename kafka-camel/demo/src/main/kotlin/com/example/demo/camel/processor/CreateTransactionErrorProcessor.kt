package com.example.demo.camel.processor

import com.example.demo.CreateTransactionErrorCommand
import com.example.demo.infrastructure.config.micrometer.CounterConfiguration
import io.micrometer.core.instrument.Counter
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class CreateTransactionErrorProcessor(
    private val counters: Map<String, Counter>
) : Processor {

    override fun process(exchange: Exchange) {
        val transactionError = exchange.getIn().getBody(CreateTransactionErrorCommand::class.java)
        println("Transaction Error with companyCode ${transactionError.transaction.companyCode} and type ${transactionError.errorType} is received")
        counters.getValue(CounterConfiguration.PROCESSED_CREATE_TRANSACTION_ERROR_MESSAGE_COUNTER).increment()
    }
}
