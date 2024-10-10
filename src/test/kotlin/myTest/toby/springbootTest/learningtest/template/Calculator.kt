package myTest.toby.springbootTest.learningtest.template

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class Calculator {
    @Throws(IOException::class)
    fun calcSum(filepath: String): Int {
        val sumCallback: LineCallback<Int> =
            object : LineCallback<Int> {
                override fun doSomethingWithLine(line: String, value: Int): Int {
                    return value + line.toInt()
                }
            }
        return lineReadTemplate(filepath, sumCallback, 0)
    }

    @Throws(IOException::class)
    fun calcMultiply(filepath: String): Int {
        val multiplyCallback: LineCallback<Int> =
            object : LineCallback<Int> {
                override fun doSomethingWithLine(line: String, value: Int): Int {
                    return value * line.toInt()
                }
            }
        return lineReadTemplate(filepath, multiplyCallback, 1)
    }

    @Throws(IOException::class)
    fun concatenate(filepath: String): String {
        val concatenateCallback: LineCallback<String> =
            object : LineCallback<String> {
                override fun doSomethingWithLine(line: String, value: String): String {
                    return value + line
                }
            }
        return lineReadTemplate(filepath, concatenateCallback, "")
    }

    @Throws(IOException::class)
    fun <T> lineReadTemplate(filepath: String, callback: LineCallback<T>, initVal: T): T {
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader(filepath))
            var res = initVal
            var line: String? = null
            while ((br.readLine().also { line = it }) != null) {
                res = callback.doSomethingWithLine(line!!, res)
            }
            return res
        } catch (e: IOException) {
            println(e.message)
            throw e
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    println(e.message)
                }
            }
        }
    }

}