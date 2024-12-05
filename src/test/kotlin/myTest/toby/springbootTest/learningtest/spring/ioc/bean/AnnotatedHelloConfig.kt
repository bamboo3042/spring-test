package myTest.toby.springbootTest.learningtest.spring.ioc.bean

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnnotatedHelloConfig {
    @Bean
    fun annotatedHello(): AnnotatedHello {
        return AnnotatedHello()
    }
}