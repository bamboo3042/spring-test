package myTest.toby.springbootTest.learningtest.pointcut

abstract class Target: TargetInterface {
    override fun hello() {}

    override fun hello(a: String) {}

    override fun plus(a: Int, b: Int): Int {
        return 0
    }

    @Throws(RuntimeException::class)
    override fun minus(a: Int, b: Int): Int {
        return 0
    }

    fun method() {}
}