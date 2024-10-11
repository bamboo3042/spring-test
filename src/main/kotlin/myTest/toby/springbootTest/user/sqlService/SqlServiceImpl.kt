package myTest.toby.springbootTest.user.sqlService

class SqlServiceImpl: SqlService {
    private lateinit var sqlMap: Map<String, String>

    fun setSqlMap(sqlMap: Map<String, String>) {
        this.sqlMap = sqlMap
    }

    @Throws(SqlRetrievalFailureException::class)
    override fun getSql(key: String): String {
        val sql = sqlMap[key] ?: throw SqlRetrievalFailureException("Sql not found for key: $key")

        return sql
    }
}