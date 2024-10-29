package myTest.toby.springbootTest.user

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.*
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.Driver
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = ["myTest.toby.springbootTest.user.dao"])
@Import(SqlServiceContext::class)
@PropertySource("classpath:database.properties")
class AppContext {
    @Value("\${db.driverClass}")
    lateinit var driverClass: Class<out Driver>
    @Value("\${db.url}")
    lateinit var url: String
    @Value("\${db.username}")
    lateinit var username: String
    @Value("\${db.password}")
    lateinit var password: String

    @Bean
    fun dataSource(): DataSource {
        val ds = SimpleDriverDataSource()

        ds.setDriverClass(this.driverClass)
        ds.url = this.url
        ds.username = this.username
        ds.password = this.password

        return ds
    }

    @Bean
    fun embeddedDatabase(): EmbeddedDatabase {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .build()
    }

    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    companion object {
        @Configuration
        @Profile("production")
        class ProductionAppContext {
            @Bean
            fun mailSender(): MailSender {
                return JavaMailSenderImpl().apply {
                    this.host = "localhost"
                }
            }
        }

        @Bean
        fun placeholderConfigurer(): PropertySourcesPlaceholderConfigurer {
            return PropertySourcesPlaceholderConfigurer()
        }
    }
}