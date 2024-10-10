package myTest.toby.springbootTest.user.service

import org.springframework.aop.ClassFilter
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.util.PatternMatchUtils

class NameMatchClassMethodPointcut: NameMatchMethodPointcut() {
    fun setMappedClassName(mappedClassName: String) {
        this.classFilter = SimpleClassFilter(mappedClassName)
    }
    
    companion object {
        class SimpleClassFilter(val mappedName: String): ClassFilter {
            override fun matches(clazz: Class<*>): Boolean {
                return PatternMatchUtils.simpleMatch(mappedName, clazz.name)
            }
        }
    }
}