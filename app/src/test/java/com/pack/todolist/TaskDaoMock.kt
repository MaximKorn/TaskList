package com.pack.todolist

import com.pack.todolist.database.Task
import com.pack.todolist.database.TaskDao
import java.util.*
import kotlin.collections.ArrayList

class TaskDaoMock : TaskDao {

    override var tasks: List<Task> = ArrayList()
    private var _tasks: List<Task> = ArrayList()

    override fun addTask(task: Task) {
        val maxId = _tasks.map { t -> t.id }.maxOrNull()
        val newId = if (maxId == null) 1 else maxId + 1
        (_tasks as ArrayList).add(Task(id = newId, name = task.name, date = task.date))

        updateTasks()
    }

    override fun removeTask(task: Task) {
        if (!_tasks.isEmpty()) {
            (_tasks as ArrayList).remove(_tasks.find { t -> t.id == task.id })
            updateTasks()
        }
    }

    override fun updateTask(task: Task) {
        if (_tasks.count { t -> t.id == task.id } == 0) {
            return
        }
        removeTask(task)
        addTask(Task(name = task.name, date = Date().toInstant().nano))

        updateTasks()
    }

    private fun updateTasks() {
        tasks = if (_tasks.size <= 10) _tasks.reversed()
                else _tasks.subList(_tasks.size - 11, _tasks.size - 1).reversed()
    }

}