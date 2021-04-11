package com.madmuv.todolist2.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(taskEntry: TaskEntry)

    @Delete
    suspend fun delete(taskEntry: TaskEntry)

    @Update
    suspend fun update(taskEntry: TaskEntry)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table ORDER BY timeStamp DESC")
    fun getAllTask(): LiveData<List<TaskEntry>>
}