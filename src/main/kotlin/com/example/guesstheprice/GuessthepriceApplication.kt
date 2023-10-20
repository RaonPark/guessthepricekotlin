package com.example.guesstheprice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class GuessthepriceApplication

fun main(args: Array<String>) {
    runApplication<GuessthepriceApplication>(*args)
}
