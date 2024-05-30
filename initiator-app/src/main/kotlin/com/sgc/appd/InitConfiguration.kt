package com.sgc.appd

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
@Configuration
class InitConfiguration() {

    @Bean("senderTemplate")
    fun senderTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.rootUri("http://vertx-demo:8888").build()
    }
}