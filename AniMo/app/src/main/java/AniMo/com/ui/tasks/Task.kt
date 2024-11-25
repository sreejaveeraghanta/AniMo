package AniMo.com.ui.tasks

data class Task(
    val name: String,
    val priority: Int,
    val date: String,
    val time: String,
    val duration: Int // Duration in minutes
)