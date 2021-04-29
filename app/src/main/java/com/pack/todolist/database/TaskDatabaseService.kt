package com.pack.todolist.database

import java.util.*

class TaskDatabaseService(val dao: TaskDao) {

    fun addTask(name: String, date: Date) {
        dao.addTask(Task(name = name, date = date.toInstant().nano))
    }

    fun removeTaskById(id: Int) {
        dao.removeTask(Task(id = id))
    }

    fun updateTask(id: Int, name: String, date: Date) {
        dao.updateTask(Task(id = id, name = name, date = date.toInstant().nano))
    }

    fun getTasks(): List<Task> {
        return dao.tasks
    }
}