package myTest.toby.springbootTest.learningtest.spring.ioc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

class ScopeTest {
    @Test
    fun singletonScope() {
        val ac = AnnotationConfigApplicationContext(AppConfig::class.java)

        val beans = mutableSetOf<SingletonBean>()
        beans.add(ac.getBean(SingletonBean::class.java))
        beans.add(ac.getBean(SingletonBean::class.java))
        assertThat(beans.size).isEqualTo(1)

        val singletonClientBean = ac.getBean(SingletonClientBean::class.java)
        beans.add(singletonClientBean.bean1)
        beans.add(singletonClientBean.bean2)
        assertThat(beans.size).isEqualTo(1)
    }

    @Test
    fun prototypeScope() {
        val ac = AnnotationConfigApplicationContext(AppConfig::class.java)
        val bean = mutableSetOf<PrototypeBean>()

        bean.add(ac.getBean(PrototypeBean::class.java))
        assertThat(bean.size).isEqualTo(1)
        bean.add(ac.getBean(PrototypeBean::class.java))
        assertThat(bean.size).isEqualTo(2)
        bean.add(ac.getBean(PrototypeClientBean::class.java).bean1)
        assertThat(bean.size).isEqualTo(3)
        bean.add(ac.getBean(PrototypeClientBean::class.java).bean2)
        assertThat(bean.size).isEqualTo(4)
    }

    @Configuration
    class AppConfig {

        @Bean
        @Scope("singleton")
        fun singletonBean(): SingletonBean {
            return SingletonBean()
        }

        @Bean
        fun singletonClientBean(): SingletonClientBean {
            return SingletonClientBean(singletonBean(), singletonBean())
        }

        @Bean
        @Scope("prototype")
        fun prototypeBean(): PrototypeBean {
            return PrototypeBean()
        }

        @Bean
        fun prototypeClientBean(): PrototypeClientBean {
            return PrototypeClientBean(prototypeBean(), prototypeBean())
        }
    }

    class SingletonBean

    class SingletonClientBean(
        val bean1: SingletonBean,
        val bean2: SingletonBean
    )

    class PrototypeBean

    class PrototypeClientBean(
        val bean1: PrototypeBean,
        val bean2: PrototypeBean,
    )
}