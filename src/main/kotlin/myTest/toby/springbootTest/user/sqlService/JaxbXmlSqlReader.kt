package myTest.toby.springbootTest.user.sqlService

import jakarta.xml.bind.JAXBContext
import myTest.toby.springbootTest.user.sqlService.jaxb.Sqlmap
import org.springframework.core.io.ClassPathResource
import java.io.InputStream
import javax.xml.bind.JAXBException

class JaxbXmlSqlReader: SqlReader {
    private val DEFAULT_SQL_MAP_FILE = "sqlmap.xml"

    private var sqlMapFile = DEFAULT_SQL_MAP_FILE

    fun setSqlMapFile(sqlMapFile: String?) {
        if (sqlMapFile != null ) this.sqlMapFile = sqlMapFile
    }

    override fun read(sqlRegistry: SqlRegistry) {
        try {
            val jaxbContext = JAXBContext.newInstance(Sqlmap::class.java)
            val unmarshaller = jaxbContext.createUnmarshaller()
            val xmlResource = ClassPathResource(sqlMapFile)
            val inputStream: InputStream = xmlResource.inputStream

            val sqlmap = unmarshaller.unmarshal(inputStream) as Sqlmap

            for (sql in sqlmap.sql) {
                sqlRegistry.registerSql(sql.key, sql.value)
            }
        } catch (e: JAXBException) {
            throw RuntimeException(e)
        }
    }
}