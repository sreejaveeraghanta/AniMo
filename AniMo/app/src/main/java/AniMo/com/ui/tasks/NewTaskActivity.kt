package AniMo.com.ui.tasks

import AniMo.com.R
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewTaskActivity : AppCompatActivity() {
    private lateinit var taskName: EditText
    private lateinit var taskDate: EditText
    private lateinit var taskTime: EditText
    private lateinit var taskPriority: Spinner
    private lateinit var taskDuration: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private val database = FirebaseDatabase.getInstance()
    private val reference = database.reference
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        taskName = findViewById(R.id.task_name)
        taskDate = findViewById(R.id.select_date_button)
        taskTime = findViewById(R.id.select_time_button)
        taskPriority = findViewById(R.id.task_priority)
        taskDuration = findViewById(R.id.task_duration)
        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)

        val priorities = arrayOf("Low", "Medium", "High")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        taskPriority.adapter = adapter

        saveButton.setOnClickListener {
            val name = taskName.text.toString()
            val date = taskDate.text.toString()
            val time = taskTime.text.toString()
            val priority = taskPriority.selectedItemPosition
            val duration = taskDuration.text.toString().toIntOrNull() ?: 0
            val userId = auth.currentUser?.uid

            if (userId != null) {
                val newTask = Task(name, priority, date, time, duration)
                saveTaskWithDateValidation(newTask, userId)
            } else {
                Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun saveTaskWithDateValidation(newTask: Task, uid: String) {
        isDateAvailable(newTask.date, uid) { isAvailable ->
            if (isAvailable) {
                val taskId = reference.child("Users").child(uid).child("Tasks").push().key
                if (taskId != null) {
                    reference.child("Users").child(uid).child("Tasks").child(taskId).setValue(newTask)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Task added successfully!", Toast.LENGTH_SHORT).show()
                            finish() // Return to previous activity
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(this, "Error saving task: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "You already have a task on this date. Please choose another date.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isDateAvailable(newTaskDate: String, uid: String, callback: (Boolean) -> Unit) {
        reference.child("Users").child(uid).child("Tasks")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var isAvailable = true
                    for (taskSnapshot in snapshot.children) {
                        val existingTask = taskSnapshot.getValue(Task::class.java)
                        if (existingTask != null && newTaskDate == existingTask.date) {
                            isAvailable = false
                            break
                        }
                    }
                    callback(isAvailable)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@NewTaskActivity, "Error checking tasks: ${error.message}", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            })
    }
}
