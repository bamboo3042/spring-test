package myTest.toby.springbootTest.learningtest.pointcut

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.aop.aspectj.AspectJExpressionPointcut

class PointcutExpressionTest {
    @Test
    @Throws(Exception::class)
    fun pointcut() {
        targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true)
        targetClassPointcutMatches("execution(* hello(..))", true, true, false, false, false, false)
        targetClassPointcutMatches("execution(* hello())", true, false, false, false, false, false)
        targetClassPointcutMatches("execution(* hello(String))", false, true, false, false, false, false)
        targetClassPointcutMatches("execution(* meth*(..))", false, false, false, false, true, true)

        targetClassPointcutMatches("execution(* *(int,int))", false, false, true, true, false, false)
        targetClassPointcutMatches("execution(* *())", true, false, false, false, true, true)

        targetClassPointcutMatches(
            "execution(* myTest.toby.springbootTest.learningtest.pointcut.Target.*(..))",
            true,
            true,
            true,
            true,
            true,
            false
        )
        targetClassPointcutMatches(
            "execution(* myTest.toby.springbootTest.learningtest.pointcut.*.*(..))",
            true,
            true,
            true,
            true,
            true,
            true
        )
        targetClassPointcutMatches(
            "execution(* myTest.toby.springbootTest.learningtest.pointcut..*.*(..))",
            true,
            true,
            true,
            true,
            true,
            true
        )
        targetClassPointcutMatches("execution(* myTest.toby.springbootTest..*.*(..))", true, true, true, true, true, true)

        targetClassPointcutMatches("execution(* com..*.*(..))", false, false, false, false, false, false)
        targetClassPointcutMatches("execution(* *..Target.*(..))", true, true, true, true, true, false)
        targetClassPointcutMatches("execution(* *..Tar*.*(..))", true, true, true, true, true, false)
        targetClassPointcutMatches("execution(* *..*get.*(..))", true, true, true, true, true, false)
        targetClassPointcutMatches("execution(* *..B*.*(..))", false, false, false, false, false, true)

        targetClassPointcutMatches("execution(* *..TargetInterface.*(..))", true, true, true, true, false, false)

        targetClassPointcutMatches("execution(* *(..) throws Runtime*)", false, false, false, true, false, true)

        targetClassPointcutMatches("execution(int *(..))", false, false, true, true, false, false)
        targetClassPointcutMatches("execution(void *(..))", true, true, false, false, true, true)
    }

    @Throws(Exception::class)
    fun targetClassPointcutMatches(expression: String, vararg expected: Boolean) {
        pointcutMatches(expression, expected[0], Target::class.java, "hello")
        pointcutMatches(expression, expected[1], Target::class.java, "hello", String::class.java)
        pointcutMatches(expression, expected[2], Target::class.java, "plus", Int::class.java, Int::class.java)
        pointcutMatches(expression, expected[3], Target::class.java, "minus", Int::class.java, Int::class.java)
        pointcutMatches(expression, expected[4], Target::class.java, "method")
        pointcutMatches(expression, expected[5], Bean::class.java, "method")
    }

    fun pointcutMatches(expression: String, expected: Boolean, clazz: Class<*>, methodName :String, vararg args: Class<*>) {
        val pointcut = AspectJExpressionPointcut()
        pointcut.expression = expression

        assertThat(
            pointcut
                .classFilter
                .matches(clazz)
                    &&
            pointcut
                .methodMatcher
                .matches(
                    clazz.getMethod(methodName, *args),
                    clazz
                )
        ).isEqualTo(expected)
    }

    @Test
    @Throws(SecurityException::class, NoSuchMethodException::class)
    fun methodSignaturePointcut() {
        val pointcut = AspectJExpressionPointcut()

        pointcut.expression = "execution(public int myTest.toby.springbootTest.learningtest.pointcut.Target.minus(int,int) throws java.lang.RuntimeException)"

        assertThat(
            pointcut
                .classFilter
                .matches(Target::class.java)
                    &&
            pointcut
                .methodMatcher
                .matches(
                    Target::class.java.getMethod("minus", Int::class.java, Int::class.java),
                    Target::class.java
                )
        ).isTrue()

        assertThat(
            pointcut
                .classFilter
                .matches(Target::class.java)
                    &&
            pointcut
                .methodMatcher
                .matches(
                    Target::class.java.getMethod("plus", Int::class.java, Int::class.java),
                    Target::class.java
                )
        ).isFalse()

        assertThat(
            pointcut
                .classFilter
                .matches(Bean::class.java)
                    &&
            pointcut
                .methodMatcher
                .matches(
                    Target::class.java.getMethod("method"),
                    Target::class.java
                )
        ).isFalse()
    }
}