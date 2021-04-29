package com.pack.todolist.database

import androidx.room.*

@Dao
interface TaskDao {

    @Insert
    fun addTask(task: Task)

    @Delete
    fun removeTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @get:Query("select * from Task order by date desc limit 10")
    val tasks: List<Task>
}