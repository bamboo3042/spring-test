package myTest.toby.springbootTest.learningtest.spring.factorybean

import org.springframework.beans.factory.FactoryBean

class MessageFactoryBean : FactoryBean<Message> {
    lateinit var text: String

    @Throws(Exception::class)
    override fun getObject(): Message {
        return Message.newMessage(this.text)
    }

    override fun getObjectType(): Class<out Message?> {
        return Message::class.java
    }

    override fun isSingleton(): Boolean {
        return true
    }
}