package myTest.toby.springbootTest.learningtest.spring.factorybean

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class FactoryBeanConfig {
    @Bean
    fun message(): MessageFactoryBean {
        return MessageFactoryBean().apply {
            this.text = "Factory Bean"
        }
    }
}