package AniMo.com.ui.tasks

import AniMo.com.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class TaskAdapter(private var tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name_view)
        val taskDate: TextView = itemView.findViewById(R.id.task_date_view)
        val taskTime: TextView = itemView.findViewById(R.id.task_time_view)
        val taskPriority: TextView = itemView.findViewById(R.id.task_priority_view)
        val taskDuration: TextView = itemView.findViewById(R.id.task_duration_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.name
        holder.taskDate.text = task.date
        holder.taskTime.text = task.time
        holder.taskPriority.text = task.priority.toString()
        holder.taskDuration.text = task.duration.toString()
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}
