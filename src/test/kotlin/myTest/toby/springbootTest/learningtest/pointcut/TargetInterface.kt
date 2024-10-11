package myTest.toby.springbootTest.learningtest.pointcut;

interface TargetInterface {
	fun hello();
	fun hello(a: String)
	fun plus(a: Int, b: Int): Int
	fun minus(a: Int, b: Int): Int
}