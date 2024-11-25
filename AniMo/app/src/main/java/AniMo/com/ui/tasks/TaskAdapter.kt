package AniMo.com.ui.tasks

import AniMo.com.R
import AniMo.com.ui.tasks.Task
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.task_name_view)
        val taskPriority: TextView = view.findViewById(R.id.task_priority_view)
        val taskDate: TextView = view.findViewById(R.id.task_date_view)
        val taskTime: TextView = view.findViewById(R.id.task_time_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.name
        holder.taskPriority.text = "Priority: ${task.priority}"
        holder.taskDate.text = "Date: ${task.date}"
        holder.taskTime.text = "Time: ${task.time}"
    }

    override fun getItemCount(): Int = tasks.size
}