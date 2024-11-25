package AniMo.com.ui.tasks


import AniMo.com.R
import AniMo.com.ui.home.HomeFragment
import AniMo.com.ui.tasks.Task
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addTaskButton: Button
    private lateinit var backToHomeButton: Button
    private lateinit var historyButton: Button
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        recyclerView = findViewById(R.id.task_recycler_view)
        addTaskButton = findViewById(R.id.add_task_button)
        backToHomeButton = findViewById(R.id.back_to_home_button)
        historyButton = findViewById(R.id.history_button)


        recyclerView.layoutManager = LinearLayoutManager(this)
        loadTasks()
        val adapter = TaskAdapter(taskList)
        recyclerView.adapter = adapter


        addTaskButton.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
        }

        backToHomeButton.setOnClickListener {
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
        }

    }

    private fun loadTasks() {
        val sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("task_list", null)
        val type = object : TypeToken<MutableList<Task>>() {}.type
        val gson = Gson()
        val savedTasks: MutableList<Task>? = gson.fromJson(json, type)
        if (savedTasks != null) {
            taskList.addAll(savedTasks)
        }
    }
}