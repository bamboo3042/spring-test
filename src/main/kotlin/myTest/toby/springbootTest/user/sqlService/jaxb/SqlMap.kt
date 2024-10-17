package myTest.toby.springbootTest.user.sqlService.jaxb

import jakarta.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["sql"])
@XmlRootElement(name = "sqlmap", namespace = "http://www.epril.com/sqlmap")
data class Sqlmap(
    @XmlElementRef(name = "sql", namespace = "http://www.epril.com/sqlmap", type = SqlType::class)
    var sql: MutableList<SqlType> = mutableListOf()
) {
    fun getSqlList(): List<SqlType> {
        return this.sql
    }
}