package AniMo.com.ui.tasks

import AniMo.com.R
import AniMo.com.ui.tasks.Task
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val taskList: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.name
        holder.taskPriority.text = "Priority: ${task.priority}"
        holder.taskDate.text = task.date
        holder.taskTime.text = task.time
        holder.taskDuration.text = "Duration: ${task.duration} mins"
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name_view)
        val taskPriority: TextView = itemView.findViewById(R.id.task_priority_view)
        val taskDate: TextView = itemView.findViewById(R.id.task_date_view)
        val taskTime: TextView = itemView.findViewById(R.id.task_time_view)
        val taskDuration: TextView = itemView.findViewById(R.id.task_duration_view)
    }
}