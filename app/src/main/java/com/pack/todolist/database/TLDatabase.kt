package com.pack.todolist.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 2)
abstract class TLDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao?
}