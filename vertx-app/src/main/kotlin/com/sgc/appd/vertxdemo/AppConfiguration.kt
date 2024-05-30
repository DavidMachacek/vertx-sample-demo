package com.sgc.appd.vertxdemo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.vertx.core.Vertx
import io.vertx.core.spi.VerticleFactory
import io.vertx.mssqlclient.MSSQLConnectOptions
import io.vertx.mssqlclient.MSSQLPool
import io.vertx.sqlclient.PoolOptions
import jakarta.persistence.Persistence
import mu.KotlinLogging
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@Configuration
open class AppConfiguration {

    private val logger = KotlinLogging.logger {}

    @Bean
    open fun sessionFactory(): SessionFactory {
        val sessionFactory =  Persistence.createEntityManagerFactory("petsdb")
            .unwrap(SessionFactory::class.java)
        logger.info("✅ Hibernate Reactive is ready")
        return sessionFactory
    }

    @Bean
    open fun vertx(verticleFactory: VerticleFactory): Vertx {
        val vertx = Vertx.vertx()
        vertx.registerVerticleFactory(verticleFactory)
        return vertx
    }

    @Bean
    open fun objectMapper(): ObjectMapper {

        val objectMapper = ObjectMapper()
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        objectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)

        val module = JavaTimeModule()
        objectMapper.registerModule(module)
        return objectMapper
    }

    // this is useless (persistence.xml is used instead). But can be used to init SQL client
    @Bean
    open fun mssqlPool(vertx: Vertx): MSSQLPool {
        val connectOptions = MSSQLConnectOptions()
        connectOptions.host = "sgc-test-db-server.database.windows.net:1433"
        connectOptions.user = "test@sgc-test-db-server"
        connectOptions.password = "SGConsulting123!"
        connectOptions.database = "test-azure-db"
        val poolOptions = PoolOptions()
        poolOptions.idleTimeout = 5
        poolOptions.idleTimeoutUnit = TimeUnit.MINUTES
        poolOptions.maxSize = 5
        return MSSQLPool.pool(vertx, connectOptions, poolOptions)
    }
}