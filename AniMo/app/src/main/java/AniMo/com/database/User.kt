package AniMo.com.database

data class User(
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val hearts: Int = 0,
    val friends: Int = 0,
    val tasksCompleted: Int = 0,
    val visitors: Int = 0,
    val visited: Int = 0,
    val timeSpent: Double = 0.0,
    val friendList: List<String> = listOf(),
    val backgroundsOwned: List<String> = listOf(),
    val musicOwned: List<String> = listOf(),
    val backgroundEquipped: String = "",
)
