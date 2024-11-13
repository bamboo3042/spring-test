package myTest.toby.springbootTest.learningtest.spring.ioc.bean

class StringPrinter: Printer {
    private val buffer = StringBuffer()

    override fun printe(message: String) {
        this.buffer.append(message)
    }

    override fun toString(): String {
        return this.buffer.toString()
    }
}