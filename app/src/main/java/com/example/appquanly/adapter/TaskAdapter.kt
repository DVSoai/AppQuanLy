package com.example.appquanly.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appquanly.R

class TaskAdapter(private var tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val taskNameTextView: TextView
        val taskDateTextView: TextView


        init {
            taskNameTextView = view.findViewById(R.id.textViewTaskName)
            taskDateTextView = view.findViewById(R.id.textViewTaskDate)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.taskNameTextView.text= tasks[position].taskName
        viewHolder.taskDateTextView.text=tasks[position].taskDate

    }

    override fun getItemCount(): Int {
        return tasks.size
    }
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }


}