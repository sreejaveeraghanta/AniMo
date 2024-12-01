package AniMo.com.database

data class User(
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val hearts: Int = 0,
    val treats: Int = 0,
    val tasksCompleted: Int = 0,
    val visitors: Int = 0,
    val visited: Int = 0,
    val timeSpent: Double = 0.0,
)
