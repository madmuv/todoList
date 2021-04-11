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

    @Query("delete from task_table")
    suspend fun deleteAll()

    @Query("select * from task_table order by timeStamp desc")
    fun getAllTask(): LiveData<List<TaskEntry>>

    @Query("select * from task_table order by priority asc")
    fun getAllPriorityTask(): LiveData<List<TaskEntry>>

    @Query("select * from task_table where title like :searchQuery order by timeStamp desc")
    fun searchDatabase(searchQuery: String): LiveData<List<TaskEntry>>
}