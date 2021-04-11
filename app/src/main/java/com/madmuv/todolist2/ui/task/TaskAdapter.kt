package com.madmuv.todolist2.ui.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.madmuv.todolist2.databinding.ItemTaskBinding
import com.madmuv.todolist2.db.TaskEntry

class TaskAdapter : ListAdapter<TaskEntry, TaskAdapter.ViewHolder>(TaskDiffCallBack){

    companion object TaskDiffCallBack : DiffUtil.ItemCallback<TaskEntry>() {
        override fun areItemsTheSame(oldItem: TaskEntry, newItem: TaskEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskEntry, newItem: TaskEntry): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntry: TaskEntry){
            binding.taskEntry = taskEntry
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {
        return ViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}