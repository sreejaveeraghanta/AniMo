package AniMo.com

import AniMo.com.database.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


object Util {

    var database = FirebaseDatabase.getInstance()
    var reference = database.reference.child("Users")

    fun updateStats(uid: String, hearts: Int, tasks: Int, timeSpent: Double, visitors: Int, visited: Int, friends:Int) {
        CoroutineScope(IO).launch {
            reference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        val totalHearts = user.hearts + hearts
                        val totalTasks = user.tasksCompleted + tasks
                        val totalTime = user.timeSpent + timeSpent
                        val totalVisitors = user.visitors + visitors
                        val totalVisited = user.visited + visited
                        val totalFriends = user.friends + friends

                        val updates = hashMapOf<String, Any> (
                            "hearts" to totalHearts,
                            "visitors" to totalVisitors,
                            "visited" to totalVisited,
                            "timeSpent" to totalTime,
                            "tasksCompleted" to totalTasks,
                            "friends" to totalFriends
                        )
                        reference.child(uid).updateChildren(updates)
                    }
                }
            }

        }
    }
}