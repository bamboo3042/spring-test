package myTest.toby.springbootTest.user.sqlService

class DefaultSqlService: BaseSqlService() {
    init {
        super.sqlRegistry = HashMapSqlRegistry()
        super.sqlReader = JaxbXmlSqlReader()
    }
}