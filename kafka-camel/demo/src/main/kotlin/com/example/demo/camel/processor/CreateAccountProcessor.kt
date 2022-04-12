package com.example.demo.camel.processor

import io.micrometer.core.instrument.Counter
import com.example.demo.TransactionDto
import com.example.demo.infrastructure.config.micrometer.CounterConfiguration
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class CreateAccountProcessor(
    private val counters: Map<String, Counter>
) : Processor {
    override fun process(exchange: Exchange) {
        val transaction = exchange.getIn().getBody(TransactionDto::class.java)
        println("Transaction with companyCode ${transaction.companyCode} is received")
        counters.getValue(CounterConfiguration.PROCESSED_CREATE_ACCOUNT_MESSAGE_COUNTER).increment()
    }
}
