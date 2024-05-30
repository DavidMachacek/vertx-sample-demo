package com.sgc.appd.vertxdemo

import io.vertx.core.Vertx
import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
open class VertxSpringApplication {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var mainVerticle: MainVerticle

    @Autowired
    lateinit var petService: PetService

    @PostConstruct
    open fun deployVerticle() {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(mainVerticle)
        vertx.deployVerticle(petService)
    }
}

fun main(args: Array<String>) {
    runApplication<VertxSpringApplication>(*args)
}
