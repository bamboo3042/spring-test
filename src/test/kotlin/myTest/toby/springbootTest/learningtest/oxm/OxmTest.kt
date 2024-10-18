package myTest.toby.springbootTest.learningtest.oxm

import myTest.toby.springbootTest.user.sqlService.jaxb.Sqlmap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.oxm.Unmarshaller
import org.springframework.oxm.XmlMappingException
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.io.IOException
import javax.xml.transform.stream.StreamSource

@ExtendWith(SpringExtension::class)
@SpringBootTest
class OxmTest {
    @Autowired
    lateinit var unmarshaller: Unmarshaller

    @Test
    @Throws(XmlMappingException::class, IOException::class)
    fun unmarshallSqlMap() {
        val xmlSource = StreamSource(javaClass.getResourceAsStream("/testSqlmap.xml"))
        val sqlmap = unmarshaller.unmarshal(xmlSource) as Sqlmap

        val sqlList = sqlmap.sql

        assertThat(sqlList.size).isEqualTo(3)
        assertThat(sqlList[0].value).isEqualTo("insert")
        assertThat(sqlList[0].key).isEqualTo("add")
        assertThat(sqlList[1].key).isEqualTo("get")
        assertThat(sqlList[1].value).isEqualTo("select")
        assertThat(sqlList[2].key).isEqualTo("delete")
        assertThat(sqlList[2].value).isEqualTo("delete")
    }
}