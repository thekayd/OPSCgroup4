package com.kayodedaniel.signinloginpoe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val taskList: List<UserTask>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.textViewTaskName.text = currentTask.name
        holder.textViewCategory.text = currentTask.category
        holder.textViewDate.text = currentTask.date
        holder.textViewTime.text = "${currentTask.startTime} - ${currentTask.endTime}"
        holder.textViewDescription.text = currentTask.description
        // You can bind image here if needed
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTaskName: TextView = itemView.findViewById(R.id.text_view_task_name)
        val textViewCategory: TextView = itemView.findViewById(R.id.text_view_category)
        val textViewDate: TextView = itemView.findViewById(R.id.text_view_date)
        val textViewTime: TextView = itemView.findViewById(R.id.text_view_time)
        val textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        // Add ImageView if needed
    }
}
