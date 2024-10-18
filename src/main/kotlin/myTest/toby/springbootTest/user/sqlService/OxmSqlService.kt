package myTest.toby.springbootTest.user.sqlService

import jakarta.annotation.PostConstruct
import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.sqlService.jaxb.Sqlmap
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.Unmarshaller
import java.io.IOException
import javax.xml.transform.stream.StreamSource

class OxmSqlService(
    unmarshaller: Unmarshaller,
    sqlmap: Resource? = null
): SqlService {
    private val baseSqlService = BaseSqlService()
    private val oxmSqlReader = OxmSqlReader(unmarshaller).also { if (sqlmap != null) it.setSqlmap(sqlmap) }
    private val sqlRegistry = HashMapSqlRegistry()

    @PostConstruct
    fun loadSql() {
        baseSqlService.sqlReader = oxmSqlReader
        baseSqlService.sqlRegistry = sqlRegistry

        baseSqlService.loadSql()
    }

    override fun getSql(key: String): String {
        return baseSqlService.getSql(key)
    }

    private class OxmSqlReader(
        private val unmarshaller: Unmarshaller,
    ): SqlReader {
        private val DEFAULT_SQLMAP_FILE: String = "sqlmap.xml"
        private var sqlmap: Resource = ClassPathResource(DEFAULT_SQLMAP_FILE, UserDao::class.java)

        fun setSqlmap(sqlmap: Resource) { this.sqlmap = sqlmap }

        override fun read(sqlRegistry: SqlRegistry) {
            try {
                val source = StreamSource(sqlmap.inputStream)
                val sqlmap = this.unmarshaller.unmarshal(source) as Sqlmap

                for (sql in sqlmap.sql) {
                    sqlRegistry.registerSql(sql.key, sql.value)
                }
            } catch (e: IOException) {
                throw IllegalArgumentException("${this.sqlmap} Error reading from Oxm sql data", e)
            }
        }
    }
}