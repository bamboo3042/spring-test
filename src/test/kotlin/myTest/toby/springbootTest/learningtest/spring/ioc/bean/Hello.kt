package myTest.toby.springbootTest.learningtest.spring.ioc.bean

class Hello {
    lateinit var name: String
    lateinit var printer: Printer

    fun sayHello(): String {
        return "Hello $name"
    }

    fun print() {
        this.printer.printe(sayHello())
    }
}