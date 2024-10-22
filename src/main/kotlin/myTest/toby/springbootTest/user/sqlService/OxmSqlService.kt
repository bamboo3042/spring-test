package myTest.toby.springbootTest.user.sqlService

import jakarta.annotation.PostConstruct
import myTest.toby.springbootTest.user.dao.UserDao
import myTest.toby.springbootTest.user.sqlService.jaxb.Sqlmap
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.Unmarshaller
import java.io.IOException
import javax.xml.transform.stream.StreamSource

class OxmSqlService: SqlService {
    private val baseSqlService = BaseSqlService()
    private val oxmSqlReader = OxmSqlReader()
    private var sqlRegistry: SqlRegistry = HashMapSqlRegistry()

    fun setUnMarshaller(unmarshaller: Unmarshaller) { oxmSqlReader.setUnMarshaller(unmarshaller) }
    fun setSqlmap(sqlmap: Resource) { oxmSqlReader.setSqlmap(sqlmap) }
    fun setSqlRegistry(sqlRegistry: SqlRegistry) { this.sqlRegistry = sqlRegistry }

    @PostConstruct
    fun loadSql() {
        baseSqlService.sqlReader = oxmSqlReader
        baseSqlService.sqlRegistry = sqlRegistry

        baseSqlService.loadSql()
    }

    override fun getSql(key: String): String {
        return baseSqlService.getSql(key)
    }

    private class OxmSqlReader: SqlReader {
        private val DEFAULT_SQLMAP_FILE: String = "sqlmap.xml"
        private var sqlmap: Resource = ClassPathResource(DEFAULT_SQLMAP_FILE, UserDao::class.java)
        private lateinit var unmarshaller: Unmarshaller

        fun setSqlmap(sqlmap: Resource) { this.sqlmap = sqlmap }
        fun setUnMarshaller(unmarshaller: Unmarshaller) { this.unmarshaller = unmarshaller }

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