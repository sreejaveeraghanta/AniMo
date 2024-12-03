package AniMo.com.ui.tasks

import AniMo.com.R
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TaskListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.reference
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list) // Ensure you have a layout defined here
        recyclerView = findViewById(R.id.recycler_task_list)

        // Initialize TaskAdapter with an empty list
        taskAdapter = TaskAdapter(mutableListOf())

        // Set up RecyclerView with the TaskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        loadTasks()
    }

    private fun loadTasks() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            reference.child("Users").child(userId).child("Tasks")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val taskList = mutableListOf<Task>()
                        for (taskSnapshot in snapshot.children) {
                            val task = taskSnapshot.getValue(Task::class.java)
                            if (task != null) {
                                taskList.add(task)
                            }
                        }
                        taskAdapter.updateTasks(taskList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@TaskListActivity, "Error loading tasks: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveTask(task: Task) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val taskId = reference.child("Users").child(userId).child("Tasks").push().key
            if (taskId != null) {
                reference.child("Users").child(userId).child("Tasks").child(taskId)
                    .setValue(task)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error adding task", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
