package myTest.toby.springbootTest.learningtest.template

interface LineCallback<T> {
    fun doSomethingWithLine(line: String, value: T): T
}