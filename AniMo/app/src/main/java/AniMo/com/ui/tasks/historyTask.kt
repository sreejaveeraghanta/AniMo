package AniMo.com.ui.tasks

import AniMo.com.R
import AniMo.com.database.User
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class historyTask : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private var taskList: MutableList<Task> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Set the title of the activity
        title = "Completed Tasks"

        // Initialize Firebase and RecyclerView
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        taskRecyclerView = findViewById(R.id.history_recycler_view)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList)
        taskRecyclerView.adapter = taskAdapter

        loadCompletedTasks()

        // Set up navigation buttons
        findViewById<View>(R.id.history_recycler_view).setOnClickListener {
            // Navigate back to the main task page
        }
        findViewById<View>(R.id.back_to_task_list_button).setOnClickListener {
            finish()
        }
    }

    private fun loadCompletedTasks() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val userRef = database.getReference("users/$userId")

        userRef.get().addOnSuccessListener { snapshot ->
            val currentUser = snapshot.getValue(User::class.java)
            if (currentUser != null) {
                val currentDate = LocalDate.now()
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                // Filter and display completed tasks
                val completedTasks = currentUser.tasks.filter { task ->
                    val taskDate = LocalDate.parse(task.date, dateFormatter)
                    task.isCompleted && taskDate.isBefore(currentDate)
                }

                taskList.clear()
                taskList.addAll(completedTasks)
                taskAdapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load completed tasks.", Toast.LENGTH_SHORT).show()
        }
    }
}
