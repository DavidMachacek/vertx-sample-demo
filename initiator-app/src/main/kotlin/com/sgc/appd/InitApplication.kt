package com.sgc.appd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class InitApplication

fun main(args: Array<String>) {
    runApplication<InitApplication>(*args)
}
