package com.example.demo

import com.example.demo.infrastructure.config.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class KafkaCamelApplication

fun main(args: Array<String>) {
	runApplication<KafkaCamelApplication>(*args)
}
