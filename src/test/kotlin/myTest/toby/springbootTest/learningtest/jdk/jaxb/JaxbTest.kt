package myTest.toby.springbootTest.learningtest.jdk.jaxb

import jakarta.xml.bind.JAXBContext
import myTest.toby.springbootTest.user.sqlService.jaxb.Sqlmap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.io.InputStream

class JaxbTest {

    @Test
    fun readSqlmap() {
        val jaxbContext = JAXBContext.newInstance(Sqlmap::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        val xmlResource = ClassPathResource("testSqlmap.xml")
        val inputStream: InputStream = xmlResource.inputStream

        val sqlmap = unmarshaller.unmarshal(inputStream) as Sqlmap
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