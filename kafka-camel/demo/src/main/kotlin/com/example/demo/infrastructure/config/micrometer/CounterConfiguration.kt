package com.example.demo.infrastructure.config.micrometer


import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CounterConfiguration(private val meterRegistry: MeterRegistry) {

    @Bean(PROCESSED_CREATE_ACCOUNT_MESSAGE_COUNTER)
    fun processedCreateAccountMessageCounter() = Counter.builder("CreateAccountMessage")
        .tag("type", "processed")
        .register(meterRegistry)

    @Bean(FAILED_CREATE_ACCOUNT_MESSAGE_COUNTER)
    fun failedCreateAccountMessageCounter() = Counter.builder("CreateAccountMessage")
        .tag("type", "failed")
        .register(meterRegistry)

    @Bean(PROCESSED_CREATE_TRANSACTION_ERROR_MESSAGE_COUNTER)
    fun processedCreateTransactionErrorMessageCounter() = Counter.builder("CreateTransactionErrorMessage")
        .tag("type", "processed")
        .register(meterRegistry)

    @Bean(FAILED_CREATE_TRANSACTION_ERROR_MESSAGE_COUNTER)
    fun failedCreateTransactionErrorMessageCounter() = Counter.builder("CreateTransactionErrorMessage")
        .tag("type", "failed")
        .register(meterRegistry)

    @Bean(PROCESSED_IMPORT_TRANSACTION_MESSAGE_COUNTER)
    fun processedImportTransactionMessageCounter() = Counter.builder("ImportTransactionMessage")
        .tag("type", "processed")
        .register(meterRegistry)

    @Bean(FAILED_IMPORT_TRANSACTION_MESSAGE_COUNTER)
    fun failedImportTransactionErrorMessageCounter() = Counter.builder("ImportTransactionMessage")
        .tag("type", "failed")
        .register(meterRegistry)

    companion object BeanNames {
        const val PROCESSED_CREATE_ACCOUNT_MESSAGE_COUNTER = "processedCreateAccountMessageCounter"
        const val FAILED_CREATE_ACCOUNT_MESSAGE_COUNTER = "failedCreateAccountMessageCounter"
        const val PROCESSED_CREATE_TRANSACTION_ERROR_MESSAGE_COUNTER = "processedCreateTransactionErrorMessageCounter"
        const val FAILED_CREATE_TRANSACTION_ERROR_MESSAGE_COUNTER = "failedCreateTransactionErrorMessageCounter"
        const val PROCESSED_IMPORT_TRANSACTION_MESSAGE_COUNTER = "processedImportTransactionMessageCounter"
        const val FAILED_IMPORT_TRANSACTION_MESSAGE_COUNTER = "failedImportTransactionMessageCounter"
    }
}
