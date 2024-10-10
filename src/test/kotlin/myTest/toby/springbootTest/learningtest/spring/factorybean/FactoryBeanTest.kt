package myTest.toby.springbootTest.learningtest.spring.factorybean

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration

@Import(FactoryBeanConfig::class)
@SpringBootTest
class FactoryBeanTest {
    @Autowired
    var context: ApplicationContext? = null

    @Test
    fun getMessageFromFactoryBean() {
        val message = context!!.getBean("message")

        assertThat(message.javaClass).isEqualTo(Message::class.java)
        assertThat((message as Message).getText()).isEqualTo("Factory Bean")
    }

    @Test
    @Throws(Exception::class)
    fun getFactoryBean() {
        val factory = context!!.getBean("&message")

        assertThat(factory.javaClass).isEqualTo(MessageFactoryBean::class.java)
    }

}