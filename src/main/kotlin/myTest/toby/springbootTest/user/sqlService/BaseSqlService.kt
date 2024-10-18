package myTest.toby.springbootTest.user.sqlService

import jakarta.annotation.PostConstruct

open class BaseSqlService: SqlService{
    lateinit var sqlReader: SqlReader
    lateinit var sqlRegistry: SqlRegistry

    @PostConstruct
    fun loadSql() {
        this.sqlReader.read(this.sqlRegistry)
    }

    override fun getSql(key: String): String {
        return try {
            this.sqlRegistry.findSql(key)
        }
        catch (e: SqlNotfoundException) {
            throw SqlRetrievalFailureException(e)
        }
    }
}