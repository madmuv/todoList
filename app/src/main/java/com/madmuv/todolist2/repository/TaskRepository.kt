package com.madmuv.todolist2.repository

import androidx.lifecycle.LiveData
import com.madmuv.todolist2.db.TaskDao
import com.madmuv.todolist2.db.TaskEntry

class TaskRepository(val taskDao: TaskDao) {

    suspend fun insert(taskEntry: TaskEntry) = taskDao.insert(taskEntry)

    suspend fun update(taskEntry: TaskEntry) = taskDao.update(taskEntry)

    suspend fun deleteItem(taskEntry: TaskEntry) = taskDao.delete(taskEntry)

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    fun getAllTask() : LiveData<List<TaskEntry>> = taskDao.getAllTask()
}