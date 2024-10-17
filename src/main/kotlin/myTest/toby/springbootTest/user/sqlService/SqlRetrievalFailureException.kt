package myTest.toby.springbootTest.user.sqlService

class SqlRetrievalFailureException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}