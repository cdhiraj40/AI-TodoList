package com.example.aitodolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aitodolist.R
import com.example.aitodolist.data.Task

class TaskAdapter(private val onItemClicked: (Task) -> Unit) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    var taskList: List<Task> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val taskItem = taskList[position]

        holder.taskTitle.text = taskItem.title
        holder.taskDesc.text = taskItem.description

        holder.itemView.setOnClickListener {
            onItemClicked(
                taskItem
            )
        }
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val taskTitle: TextView = itemView.findViewById(R.id.task_title)
        val taskDesc: TextView = itemView.findViewById(R.id.task_desc)
    }
}