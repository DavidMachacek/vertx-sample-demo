package com.sgc.appd.vertxdemo

import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import mu.KotlinLogging
import org.springframework.stereotype.Component


@Component
class PetHandlers(
    private val petService: PetService
) {

    private val logger = KotlinLogging.logger {}

    fun findAll(routingContext: RoutingContext) {
        logger.info { "operation=getAllBegin, layer=handler" }
        petService.getAllPets()
            .subscribe()
            .with { data ->
                data.run {
                    logger.info { "operation=getAllBegin, layer=handlerSubscriber, response=$data" }
                    routingContext.response()
                        .setChunked(true)
                        .putHeader("content-type", "application/json")
                    routingContext.response().end(Json.encode(data)).also {
                        logger.info { "operation=getAllEnd, layer=handlerSubscriber" }
                    }
                }
            }.also {
                logger.info { "operation=getAllEnd, layer=handler" }
            }
    }

    fun deleteAll(routingContext: RoutingContext) {
        logger.info { "============== DELETE ==============" }
        logger.info { "operation=deleteAllBegin, layer=handler" }
        petService.deleteAllPets()
            .subscribe()
            .with {
                it.run {
                    logger.info { "operation=deleteAllBegin, layer=handlerSubscriber" }
                    routingContext.response()
                        .setStatusCode(201)
                        .end().also {
                            logger.info { "operation=deleteAllEnd, layer=handlerSubscriber" }
                        }
                }
            }.also {
                logger.info { "operation=deleteAllAllEnd, layer=handler" }
            }
    }

    fun save(routingContext: RoutingContext) {
        val pet = routingContext.bodyAsJson.mapTo(PetDTO::class.java)
        logger.info { "operation=saveBegin, layer=handler, body=$pet" }
        petService.savePet(pet = pet)
            .subscribe()
            .with {
                it.run {
                    logger.info { "operation=saveBegin, layer=handlerSubscriber, action=response" }
                    routingContext.response()
                        .setStatusCode(201)
                        .end().also {
                            logger.info { "operation=saveEnd, layer=handlerSubscriber" }
                        }
                }
            }.also {
                logger.info { "operation=saveEnd, layer=handler" }
            }
    }
}