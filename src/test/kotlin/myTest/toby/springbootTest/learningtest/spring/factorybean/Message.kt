package myTest.toby.springbootTest.learningtest.spring.factorybean

class Message(private var text: String) {

    fun getText(): String {
        return text
    }

    companion object {
        fun newMessage(text: String): Message {
            return Message(text)
        }
    }
}