package myTest.toby.springbootTest.learningtest.spring.ioc.bean

class ConsolePrinter: Printer {
    override fun printe(message: String) {
        println(message)
    }
}