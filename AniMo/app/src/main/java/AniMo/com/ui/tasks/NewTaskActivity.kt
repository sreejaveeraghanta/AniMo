package AniMo.com.ui.tasks

import AniMo.com.R
import AniMo.com.Util
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class NewTaskActivity : AppCompatActivity() {

    private lateinit var taskNameInput: EditText
    private lateinit var slider: SeekBar
    private lateinit var priorityDisplay: TextView
    private lateinit var allTasks: TextView
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
        allTasks = findViewById(R.id.all_tasks)

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
                    selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
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
                    val userId = firebaseAuth.currentUser?.uid
                    val currentDate = LocalDate.now()
                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val taskDate = LocalDate.parse(selectedDate, dateFormatter)
                    if (taskDate.isBefore(currentDate)) {
                        val newTask = Task(taskName, priority, selectedDate, selectedTime, duration, true)
                        Util.updateStats(userId!!, duration*10, 1, duration.toDouble(), 0, 0, 0)
                        saveTaskToFirebase(newTask)
                    }
                    else {
                        val newTask = Task(taskName, priority, selectedDate, selectedTime, duration, false)
                        saveTaskToFirebase(newTask)
                    }
                    // Save the task to Firebase under the User model

                    Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, TaskListActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        allTasks.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

        // Cancel button logic
        cancelButton.setOnClickListener {
            Toast.makeText(this, "Task creation cancelled", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveTaskToFirebase(task: Task) {
        // Check if the user is logged in
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            Log.e("FirebaseDebug", "User is not logged in.")
            Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show()
            return
        }

        // Get a reference to the user's data in the database
        val userRef = database.getReference("Users/$userId")

        // Get the current user data (User model)
        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Retrieve the current user data
                val currentUser = snapshot.getValue(User::class.java)
                if (currentUser != null) {
                    currentUser.tasks.add(task)
                    userRef.child("tasks").setValue(currentUser.tasks)
                }

            } else {
                // If snapshot does not exist, handle this case (e.g., create a new user)
                Log.e("FirebaseDebug", "User data not found.")
                Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { error ->
            // Log and handle the failure for retrieving user data
            Log.e("FirebaseDebug", "Failed to retrieve user data: ${error.message}")
            Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show()
        }
    }




}

