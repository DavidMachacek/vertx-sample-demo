package com.sgc.appd.vertxdemo

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.healthchecks.HealthCheckHandler
import io.vertx.ext.healthchecks.HealthChecks
import io.vertx.ext.healthchecks.Status
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class MainVerticle: AbstractVerticle() {

    private val logger = KotlinLogging.logger {}

    @set: Autowired
    lateinit var petHandlers: PetHandlers

    override fun start() {
        logger.info("Starting HTTP server...")

        // Configure routes
        val router: Router = routes(petHandlers)

        val options = HttpServerOptions()
        options.setLogActivity(true)
        // Create the HTTP server
        vertx.createHttpServer(options) // Handle every request using the router
            .requestHandler(router) // Start listening
            .listen(8888) // Print the port
    }

    //create routes
    private fun routes(handlers: PetHandlers): Router {

        val healthCheckHandler = HealthCheckHandler
            .createWithHealthChecks(HealthChecks.create(vertx))

        healthCheckHandler.register("dummy-health-check") { future -> future.complete(Status.OK()) }
        // Create a Router
        val router = Router.router(vertx)
        router["/pets"].produces("application/json")
            .handler(handlers::findAll)
        router.delete("/pets").produces("application/json")
            .handler(handlers::deleteAll)
        router.post("/pets").consumes("application/json")
            .handler(BodyHandler.create())
            .handler(handlers::save)
        router.get("/health").handler(healthCheckHandler);

        return router
    }
}