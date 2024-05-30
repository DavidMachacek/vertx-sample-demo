package com.sgc.appd.vertxdemo

import io.vertx.core.Promise
import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.util.concurrent.Callable


// see: https://github.com/vert-x3/vertx-examples/blob/4.x/spring-examples/spring-verticle-factory/src/main/java/io/vertx/examples/spring/verticlefactory/SpringVerticleFactory.java
@Component
class SpringAwareVerticleFactory : VerticleFactory, ApplicationContextAware {
    private var applicationContext: ApplicationContext? = null
    override fun prefix(): String {
        return "spring"
    }

    override fun createVerticle(verticleName: String, classLoader: ClassLoader, promise: Promise<Callable<Verticle>>) {
        val clazz = VerticleFactory.removePrefix(verticleName)
        promise.complete(Callable { applicationContext!!.getBean(Class.forName(clazz)) as Verticle })
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}