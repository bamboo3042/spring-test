package myTest.toby.springbootTest.learningtest.spring.factorybean

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FactoryBeanConfig {
    @Bean
    fun message(): MessageFactoryBean {
        return MessageFactoryBean().apply {
            this.text = "Factory Bean"
        }
    }
}