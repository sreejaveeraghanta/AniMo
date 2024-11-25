package AniMo.com.ui.tasks

import AniMo.com.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class NewTaskActivity: AppCompatActivity() {

    private lateinit var taskNameInput: EditText
    private lateinit var slider: SeekBar
    private lateinit var priorityDisplay: TextView
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        taskNameInput = findViewById(R.id.task_name)
        slider = findViewById(R.id.task_priority_slider)
        priorityDisplay = findViewById(R.id.priority_display)
        dateButton = findViewById(R.id.select_date_button)
        timeButton = findViewById(R.id.select_time_button)
        saveButton = findViewById(R.id.save_task_button)
        cancelButton = findViewById(R.id.cancel_task_button)

        val calendar = Calendar.getInstance()

        // Update slider value display
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

            if (taskName.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            } else {
                val newTask = Task(taskName, priority, selectedDate, selectedTime)
                saveTask(newTask)
                Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, TaskListActivity::class.java)
                startActivity(intent)
            }
        }

        // Cancel button logic
        cancelButton.setOnClickListener {
            Toast.makeText(this, "Task creation cancelled", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveTask(task: Task) {
        val sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = sharedPreferences.getString("task_list", null)
        val type = object : TypeToken<MutableList<Task>>() {}.type
        val taskList: MutableList<Task> = gson.fromJson(json, type) ?: mutableListOf()
        taskList.add(task)
        editor.putString("task_list", gson.toJson(taskList))
        editor.apply()
    }

}