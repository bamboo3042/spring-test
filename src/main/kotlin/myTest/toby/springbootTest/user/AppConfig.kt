package myTest.toby.springbootTest.user

import myTest.toby.springbootTest.user.service.DummyMailSender
import myTest.toby.springbootTest.user.sqlService.*
import myTest.toby.springbootTest.user.sqlService.jaxb.Sqlmap
import myTest.toby.springbootTest.user.sqlService.updatable.EmbeddedDbSqlRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.oxm.Unmarshaller
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class AppConfig {

    @Bean
    fun dataSource(): DataSource {
        return SimpleDriverDataSource().apply {
            setDriverClass(com.mysql.cj.jdbc.Driver::class.java)
            url = "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8"
            username = "root"
            password = "123456"
        }
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

    @Bean
    fun sqlReader(): SqlReader {
        return JaxbXmlSqlReader().also {
            it.setSqlMapFile("sqlmap.xml")
        }
    }

    @Bean
    fun sqlRegistry(embeddedDatabase: EmbeddedDatabase): EmbeddedDbSqlRegistry {
        return EmbeddedDbSqlRegistry().also {
            it.setDataSource(embeddedDatabase)
        }
    }

    @Bean
    fun sqlService(unmarshaller: Unmarshaller, sqlRegistry: SqlRegistry): SqlService {
        return OxmSqlService().also {
            it.setUnMarshaller(unmarshaller)
            it.setSqlmap(ClassPathResource("sqlmap.xml"))
            it.setSqlRegistry(sqlRegistry)
        }
    }

    @Bean
    fun mailSender(): DummyMailSender {
        return DummyMailSender()
    }

    @Bean
    fun unmarshaller(): Unmarshaller {
        return Jaxb2Marshaller().also {
            it.setClassesToBeBound(Sqlmap::class.java)
        }
    }

    @Bean
    fun entityManagerFactory(embeddedDatabase: EmbeddedDatabase): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            dataSource = embeddedDatabase
            setPackagesToScan("myTest.toby.springbootTest.user")
            jpaVendorAdapter = HibernateJpaVendorAdapter().apply {
                setGenerateDdl(true)
                setShowSql(true)
            }
        }
    }
}