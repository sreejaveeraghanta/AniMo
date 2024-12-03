package AniMo.com.ui.tasks

data class Task(
    var name: String = "",
    val priority: Int = 0,
    val date: String = "",
    val time: String = "",
    val duration: Int = 0,
    val isCompleted: Boolean = false
)