package AniMo.com.ui.tasks

import AniMo.com.R
import AniMo.com.database.User
import AniMo.com.ui.tasks.Task
import AniMo.com.ui.home.HomeFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TaskListActivity : AppCompatActivity() {

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var taskList: MutableList<Task> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        taskRecyclerView = findViewById(R.id.task_recycler_view)  // RecyclerView to display tasks
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize RecyclerView with LinearLayoutManager
        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter
        taskAdapter = TaskAdapter(taskList)
        taskRecyclerView.adapter = taskAdapter

        loadTasksFromFirebase()

        // Handle button clicks
        findViewById<View>(R.id.add_task_button).setOnClickListener {
            // Navigate to the AddTaskActivity to add a new task
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.back_to_home_button).setOnClickListener {
            // Navigate back to the home screen (e.g., HomeActivity)
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
            finish() // Optional, if you want to close the current activity
        }

        findViewById<View>(R.id.history_button).setOnClickListener {
            // Placeholder for history button (you can implement history functionality here later)
            Toast.makeText(this, "History functionality not implemented yet.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadTasksFromFirebase() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val userRef = database.getReference("users/$userId")

        userRef.get().addOnSuccessListener { snapshot ->
            val currentUser = snapshot.getValue(User::class.java)
            if (currentUser != null) {
                // Clear and add tasks to the list
                taskList.clear()
                taskList.addAll(currentUser.tasks)
                taskAdapter.notifyDataSetChanged()  // Notify adapter about data change
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load tasks.", Toast.LENGTH_SHORT).show()
        }
    }
}
