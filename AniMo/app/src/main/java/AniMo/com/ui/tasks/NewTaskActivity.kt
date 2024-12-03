package AniMo.com.ui.tasks

import AniMo.com.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import AniMo.com.database.User
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class NewTaskActivity : AppCompatActivity() {

    private lateinit var taskNameInput: EditText
    private lateinit var slider: SeekBar
    private lateinit var priorityDisplay: TextView
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var durationInput: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        // Initialize views
        taskNameInput = findViewById(R.id.task_name)
        slider = findViewById(R.id.task_priority_slider)
        priorityDisplay = findViewById(R.id.priority_display)
        dateButton = findViewById(R.id.select_date_button)
        timeButton = findViewById(R.id.select_time_button)
        durationInput = findViewById(R.id.task_duration)
        saveButton = findViewById(R.id.save_task_button)
        cancelButton = findViewById(R.id.cancel_task_button)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val calendar = Calendar.getInstance()

        // Priority slider listener
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priorityDisplay.text = "Priority: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Date picker dialog
        dateButton.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDate = "${dayOfMonth}/${month + 1}/$year"
                    dateButton.text = "Date: $selectedDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Time picker dialog
        timeButton.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                { _, hour, minute ->
                    selectedTime = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                    timeButton.text = "Time: $selectedTime"
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        // Save button logic
        saveButton.setOnClickListener {
            val taskName = taskNameInput.text.toString()
            val priority = slider.progress
            val durationText = durationInput.text.toString()

            if (taskName.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty() || durationText.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            } else {
                val duration = durationText.toIntOrNull()
                if (duration == null || duration <= 0) {
                    Toast.makeText(this, "Please enter a valid duration!", Toast.LENGTH_SHORT).show()
                } else {
                    val newTask = Task(taskName, priority, selectedDate, selectedTime, duration)

                    // Save the task to Firebase under the User model
                    saveTaskToFirebase(newTask)

                    Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, TaskListActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        // Cancel button logic
        cancelButton.setOnClickListener {
            Toast.makeText(this, "Task creation cancelled", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveTaskToFirebase(task: Task) {
        // Get the user ID (from Firebase Authentication)
        val userId = firebaseAuth.currentUser?.uid ?: return

        // Get a reference to the user's data in the database
        val userRef = database.getReference("users/$userId")

        // Get the current user data (User model)
        userRef.get().addOnSuccessListener { snapshot ->
            // If snapshot is null or empty, create a new default User
            val currentUser = snapshot.getValue(User::class.java) ?: User(
                name = "",
                email = "",
                uid = userId,
                hearts = 0,
                friends = 0,
                tasksCompleted = 0,
                visitors = 0,
                visited = 0,
                timeSpent = 0.0,
                backgroundsOwned = listOf(),
                musicOwned = listOf(),
                backgroundEquipped = "",
                tasks = mutableListOf() // Initialize tasks as an empty list if not found
            )

            // Now that we have the current user (or a default one), add the new task
            currentUser.tasks.add(task)

            // Save the updated user data with the new task
            userRef.setValue(currentUser).addOnSuccessListener {
                Log.d("FirebaseDebug", "Task saved successfully.")
                Toast.makeText(this, "Task saved successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { error ->
                Log.e("FirebaseDebug", "Failed to save task: ${error.message}")
                Toast.makeText(this, "Failed to save task. Try again.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { error ->
            // Log and handle the failure for retrieving user data
            Log.e("FirebaseDebug", "Failed to retrieve user data: ${error.message}")
            Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show()
        }
    }


}



