package com.sgc.appd

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class InitService(
    val senderTemplate: RestTemplate
) {


    private val log: Logger = LogManager.getLogger(
        InitService::class.java
    )

    var counter: Int = 0

    // called each 30s
    @Scheduled(fixedDelay = 30000)
    fun post() {
        counter++
        val pet = PetDTO(name = "animal$counter", type = PetEnum.CAT)
        log.info("operation=postObject, payload: $pet, action=sending to vertx..")
        senderTemplate.postForObject("/pets", pet, Void::class.java)
    }

    // called each 10s
    @Scheduled(fixedDelay = 10000)
    fun get() {
        log.info("operation=getAll, action=sending to vertx")
        senderTemplate.getForObject("/pets", Void::class.java)
    }

    // called each 150s
    @Scheduled(fixedDelay = 150000)
    fun delete() {
        log.info("operation=deleteAll, action=sending to vertx")
        senderTemplate.delete("/pets")
    }
}