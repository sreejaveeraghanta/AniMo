package AniMo.com.ui.tasks


import AniMo.com.R
import AniMo.com.ui.tasks.Task
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        recyclerView = findViewById(R.id.task_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadTasks()
        val adapter = TaskAdapter(taskList)
        recyclerView.adapter = adapter

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