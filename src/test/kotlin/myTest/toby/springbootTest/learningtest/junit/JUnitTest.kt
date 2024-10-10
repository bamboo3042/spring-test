package myTest.toby.springbootTest.learningtest.junit

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
class JUnitTest {
    @Autowired
    var context: ApplicationContext? = null

    @Test
    fun test1() {
        MatcherAssert.assertThat(testObjects, Matchers.not(Matchers.hasItem(this)))
        testObjects.add(this)

        MatcherAssert.assertThat(contextObject == null || contextObject === this.context, CoreMatchers.`is`(true))
        contextObject = this.context
    }

    @Test
    fun test2() {
        MatcherAssert.assertThat(testObjects, Matchers.not(Matchers.hasItem(this)))
        testObjects.add(this)

        Assertions.assertTrue(contextObject == null || contextObject === this.context)
        contextObject = this.context
    }

    @Test
    fun test3() {
        MatcherAssert.assertThat(testObjects, Matchers.not(Matchers.hasItem(this)))
        testObjects.add(this)

        MatcherAssert.assertThat(
            contextObject, Matchers.either(CoreMatchers.`is`(CoreMatchers.nullValue())).or(
                CoreMatchers.`is`(
                    contextObject
                )
            )
        )
        contextObject = this.context
    }

    companion object {
        var testObjects: MutableSet<JUnitTest> = HashSet()
        var contextObject: ApplicationContext? = null
    }
}