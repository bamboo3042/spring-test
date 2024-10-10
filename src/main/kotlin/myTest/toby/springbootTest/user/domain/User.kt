package myTest.toby.springbootTest.user.domain

class User(
    var id: String = "",
    var name: String = "",
    var password: String = "",
    var email: String = "",
    var level: Level? = null,
    var login: Int = 0,
    var recommend: Int = 0
) {
    fun upgradeLevel() {
        val nextLevel = level?.nextLevel()
        checkNotNull(nextLevel) { level.toString() + "��  ���׷��̵尡 �Ұ����մϴ�" }
        this.level = nextLevel
    }
}