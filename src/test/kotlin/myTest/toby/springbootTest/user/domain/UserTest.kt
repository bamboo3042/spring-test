package myTest.toby.springbootTest.user.domain

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserTest {
    var user: User? = null

    @BeforeEach
    fun setUp() {
        user = User()
    }

    @Test
    fun upgradeLevel() {
        val levels = Level.entries.toTypedArray()
        for (level in levels) {
            if (level.nextLevel() == null) continue
            user!!.level = (level)
            user!!.upgradeLevel()
            MatcherAssert.assertThat(user!!.level, Matchers.`is`(level.nextLevel()))
        }
    }

    @Test
    fun cannotUpgradeLevel() {
        assertThrows<IllegalStateException> {
            val levels = Level.entries.toTypedArray()
            for (level in levels) {
                if (level.nextLevel() != null) continue
                user!!.level = (level)
                user!!.upgradeLevel()
            }
        }
    }
}