package myTest.toby.springbootTest.learningtest.template

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

class CalcSumTest {
    var calculator: Calculator? = null
    var numFilepath: String? = null

    @BeforeEach
    fun setUp() {
        this.calculator = Calculator()
        this.numFilepath = javaClass.getResource("/numbers.txt")?.path
    }

    @Test
    @Throws(IOException::class)
    fun sumOfNumbers() {
        assertThat(calculator!!.calcSum(numFilepath!!)).isEqualTo(10)
    }

    @Test
    @Throws(IOException::class)
    fun multiplyOfNumbers() {
        assertThat(calculator!!.calcMultiply(numFilepath!!)).isEqualTo(24)
    }

    @Test
    @Throws(IOException::class)
    fun concatenateStrings() {
        assertThat(calculator!!.concatenate(numFilepath!!)).isEqualTo("1234")
    }

}