package myTest.toby.springbootTest.user.sqlService

interface SqlService {
    @Throws(SqlRetrievalFailureException::class)
    fun getSql(key: String): String
}