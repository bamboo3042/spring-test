package myTest.toby.springbootTest.learningtest.spring.ioc

import myTest.toby.springbootTest.learningtest.spring.ioc.bean.Hello
import myTest.toby.springbootTest.learningtest.spring.ioc.bean.Printer
import myTest.toby.springbootTest.learningtest.spring.ioc.bean.StringPrinter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.RuntimeBeanReference
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext
import org.springframework.context.support.StaticApplicationContext

class ApplicationContextTest {
    @Test
    fun registerBean() {
        val ac = StaticApplicationContext()
        ac.registerSingleton("hello1", Hello::class.java)

        val hello1 = ac.getBean("hello1", Hello::class.java)
        assertThat(hello1).isNotNull

        val helloDef: BeanDefinition = RootBeanDefinition(Hello::class.java)
        helloDef.propertyValues.addPropertyValue("name", "Spring")
        ac.registerBeanDefinition("hello2", helloDef)

        val hello2 = ac.getBean("hello2", Hello::class.java)
        assertThat(hello2.sayHello()).isEqualTo("Hello Spring")
        assertThat(hello1).isNotEqualTo(hello2)

        assertThat(ac.beanFactory.beanDefinitionCount).isEqualTo(2)
    }

    @Test
    fun registerBeanWithDependency() {
        val ac = StaticApplicationContext()

        ac.registerBeanDefinition("printer", RootBeanDefinition(StringPrinter::class.java))

        val helloDef = RootBeanDefinition(Hello::class.java)
        helloDef.propertyValues.addPropertyValue("name", "Spring")
        helloDef.propertyValues.addPropertyValue("printer", RuntimeBeanReference("printer"))

        ac.registerBeanDefinition("hello", helloDef)

        val hello = ac.getBean("hello", Hello::class.java)
        hello.print()

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring")
    }

    @Test
    fun genericApplicationContext() {
        val ac = GenericApplicationContext()
        val reader = XmlBeanDefinitionReader(ac)
        reader.loadBeanDefinitions("ioc/genericApplicationContext.xml")

        ac.refresh()

        val hello = ac.getBean("hello", Hello::class.java)
        hello.print()

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring")
    }

    @Test
    fun genericXmlApplicationContext() {
        val ac = GenericXmlApplicationContext("ioc/genericApplicationContext.xml")

        val hello = ac.getBean("hello", Hello::class.java)
        hello.print()

        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring")
    }

    @Test
    fun contextHierarchy() {
        val parent = GenericXmlApplicationContext("ioc/parentContext.xml")

        val child = GenericApplicationContext(parent)
        val reader = XmlBeanDefinitionReader(child)
        reader.loadBeanDefinitions("ioc/childContext.xml")
        child.refresh()

        val printer = child.getBean("printer", Printer::class.java)

        assertThat(printer).isNotNull

        val hello = child.getBean("hello", Hello::class.java)
        assertThat(hello).isNotNull

        hello.print()
        assertThat(printer.toString()).isEqualTo("Hello Child")
    }
}