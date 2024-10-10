package myTest.toby.springbootTest.learningtest.jdk.proxy

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.aop.ClassFilter
import org.springframework.aop.Pointcut
import org.springframework.aop.framework.ProxyFactoryBean
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

class DynamicProxyTest {
    @Test
    fun simpleProxy() {
        val hello: Hello = HelloTarget()

        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby")
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby")
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby")

        val proxiedHello = Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf<Class<*>>(Hello::class.java),
            UppercaseHandler(HelloTarget())
        ) as Hello


//		Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY")
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY")
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY")
    }

    @Test
    fun proxyFactoryBean() {
        val pfBean = ProxyFactoryBean()
        pfBean.setTarget(HelloTarget())
        pfBean.addAdvice(UppercaseAdvice())

        val proxiedHello = pfBean.`object` as Hello

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY")
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY")
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY")
    }

    @Test
    fun pointcutAdvisor() {
        val pfBean = ProxyFactoryBean()
        pfBean.setTarget(HelloTarget())

        val pointcut = NameMatchMethodPointcut()
        pointcut.setMappedNames("sayH*")

        pfBean.addAdvisor(DefaultPointcutAdvisor(pointcut, UppercaseAdvice()))

        val proxiedHello = pfBean.`object` as Hello

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY")
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY")
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby")
    }

    @Test
    fun classNamePointcutAdvisor() {
        val classMethodPointcut: NameMatchMethodPointcut = object : NameMatchMethodPointcut() {
            override fun getClassFilter(): ClassFilter {
                return ClassFilter { clazz -> clazz.simpleName.startsWith("HelloT") }
            }
        }
        classMethodPointcut.setMappedName("sayH*")

        checkAdviced(HelloTarget(), classMethodPointcut, true)

        class HelloWorld : HelloTarget()

        checkAdviced(HelloWorld(), classMethodPointcut, false)

        class HelloToby : HelloTarget()

        checkAdviced(HelloToby(), classMethodPointcut, true)
    }

    private fun checkAdviced(target: Any, pointCut: Pointcut, adviced: Boolean) {
        val pfBean = ProxyFactoryBean()
        pfBean.setTarget(target)
        pfBean.addAdvisor(DefaultPointcutAdvisor(pointCut, UppercaseAdvice()))
        val proxiedHello = pfBean.`object` as Hello

        if (adviced) {
            assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY")
            assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY")
            assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby")
        }
        else {
            assertThat(proxiedHello.sayHello("Toby")).isEqualTo("Hello Toby")
            assertThat(proxiedHello.sayHi("Toby")).isEqualTo("Hi Toby")
            assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby")
        }
    }

    companion object {
        class UppercaseAdvice : MethodInterceptor {
            @Throws(Throwable::class)
            override fun invoke(invocation: MethodInvocation): Any {
                val ret = invocation.proceed() as String
                return ret.uppercase(Locale.getDefault())
            }
        }
    }

    internal class HelloUppercase(var hello: Hello) : Hello {
        override fun sayHello(name: String): String {
            return hello.sayHello(name).uppercase(Locale.getDefault())
        }

        override fun sayHi(name: String): String {
            return hello.sayHi(name).uppercase(Locale.getDefault())
        }

        override fun sayThankYou(name: String): String {
            return hello.sayThankYou(name).uppercase(Locale.getDefault())
        }
    }

    internal class UppercaseHandler internal constructor(var target: Any) : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
            val ret = method.invoke(target, *args)
            return if (ret is String && method.name.startsWith("say")) {
                ret.uppercase(Locale.getDefault())
            } else {
                ret
            }
        }
    }

    internal interface Hello {
        fun sayHello(name: String): String
        fun sayHi(name: String): String
        fun sayThankYou(name: String): String
    }

    internal open class HelloTarget : Hello {
        override fun sayHello(name: String): String {
            return "Hello $name"
        }

        override fun sayHi(name: String): String {
            return "Hi $name"
        }

        override fun sayThankYou(name: String): String {
            return "Thank You $name"
        }
    }
}