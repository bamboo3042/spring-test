package myTest.toby.springbootTest.user.domain

enum class Level(private val value: Int, private val next: Level?) {
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    fun intValue(): Int {
        return value
    }

    fun nextLevel(): Level? {
        return this.next
    }

    companion object {
        fun valueOf(value: Int): Level {
            return when (value) {
                1 -> BASIC
                2 -> SILVER
                3 -> GOLD
                else -> throw AssertionError("Unknown value: $value")
            }
        }
    }
}