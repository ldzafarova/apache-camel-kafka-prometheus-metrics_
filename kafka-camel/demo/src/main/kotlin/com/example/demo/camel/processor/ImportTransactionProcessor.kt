package com.example.demo.camel.processor

import com.example.demo.TransactionDto
import com.example.demo.infrastructure.config.micrometer.CounterConfiguration
import io.micrometer.core.instrument.Counter
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class ImportTransactionProcessor(
    private val counters: Map<String, Counter>
) : Processor {
    override fun process(exchange: Exchange) {
        val transaction = exchange.getIn().getBody(TransactionDto::class.java)
        println("Import Transaction with companyCode ${transaction.companyCode} is received")
        counters.getValue(CounterConfiguration.PROCESSED_IMPORT_TRANSACTION_MESSAGE_COUNTER).increment()
    }
}
