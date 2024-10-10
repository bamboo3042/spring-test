package myTest.toby.springbootTest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringbootTestApplication

fun main(args: Array<String>) {
	runApplication<SpringbootTestApplication>(*args)
}
