package myTest.toby.springbootTest.user.sqlService.jaxb

import jakarta.xml.bind.annotation.*

@XmlRootElement(name = "sql", namespace = "http://www.epril.com/sqlmap")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sqlType", propOrder = ["value"])
data class SqlType(
    @XmlValue
    var value: String = "",

    @XmlAttribute(required = true)
    var key: String = ""
)