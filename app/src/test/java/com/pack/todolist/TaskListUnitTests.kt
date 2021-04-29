package com.pack.todolist

import com.pack.todolist.database.TaskDatabaseService
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.*

class TaskListUnitTests {

    private lateinit var taskDatabaseService: TaskDatabaseService

    @Before
    fun before() {
        taskDatabaseService = TaskDatabaseService(TaskDaoMock())
    }

    @Test
    fun getFromEmpty() {
        val tasks = taskDatabaseService.getTasks()
        assertEquals(0, tasks.size)
    }

    @Test
    fun getWithLessThan10Elements() {
        for (i in 1..5) {
            taskDatabaseService.addTask("test", Date())
        }
        val tasks = taskDatabaseService.getTasks()
        assertEquals(5, tasks.size)
    }

    @Test
    fun getWith10Elements() {
        for (i in 1..10) {
            taskDatabaseService.addTask("test", Date())
        }
        val tasks = taskDatabaseService.getTasks()
        assertEquals(10, tasks.size)
    }

    @Test
    fun getWithMoreThan10Elements() {
        for (i in 1..20) {
            taskDatabaseService.addTask("test", Date())
        }
        val tasks = taskDatabaseService.getTasks()
        assertEquals(10, tasks.size)
    }

    @Test
    fun add() {
        taskDatabaseService.addTask("test", Date())
        val tasks = taskDatabaseService.getTasks()
        assertArrayEquals(arrayOf("test"), tasks.map { todo -> todo.name }.toTypedArray())
    }

    @Test
    fun emptyAfterDelete() {
        taskDatabaseService.addTask("test", Date())
        val tasks = taskDatabaseService.getTasks()
        taskDatabaseService.removeTaskById(tasks.first().id)
        val tasksAfterDelete = taskDatabaseService.getTasks()
        assertArrayEquals(arrayOf(), tasksAfterDelete.map { todo -> todo.name }.toTypedArray())
    }

    @Test
    fun deleteFromEmpty() {
        taskDatabaseService.removeTaskById(1)
    }

    @Test
    fun deleteElementExists() {
        for (i in 1..2) {
            taskDatabaseService.addTask("test", Date())
        }
        val tasks = taskDatabaseService.getTasks()
        taskDatabaseService.removeTaskById(tasks.first().id)
        val tasksAfterDelete = taskDatabaseService.getTasks()
        assertArrayEquals(arrayOf(tasks.last()), tasksAfterDelete.toTypedArray())
    }

    @Test
    fun deleteElementNotExists() {
        taskDatabaseService.addTask("test", Date())
        val tasks = taskDatabaseService.getTasks()
        taskDatabaseService.removeTaskById(tasks.first().id + 1)
        val tasksAfterDelete = taskDatabaseService.getTasks()
        assertArrayEquals(tasks.toTypedArray(), tasksAfterDelete.toTypedArray())
    }

    @Test
    fun checkOrderDescAfterAdd() {
        taskDatabaseService.addTask("test1", Date())
        taskDatabaseService.addTask("test2", Date())
        val tasks = taskDatabaseService.getTasks()
        assertArrayEquals(arrayOf("test2", "test1"), tasks.map { todo -> todo.name }.toTypedArray())
    }

    @Test
    fun checkOrderDescAfterUpdate() {
        taskDatabaseService.addTask("test1", Date())
        taskDatabaseService.addTask("test2", Date())
        val tasks = taskDatabaseService.getTasks()
        taskDatabaseService.updateTask(tasks.last().id, "test3", Date())
        val tasksAfterUpdate = taskDatabaseService.getTasks()
        assertArrayEquals(arrayOf("test3", "test2"), tasksAfterUpdate.map { todo -> todo.name }.toTypedArray())

    }

    @Test
    fun checkOrderDescAfterDelete() {
        taskDatabaseService.addTask("test1", Date())
        taskDatabaseService.addTask("test2", Date())
        taskDatabaseService.addTask("test3", Date())
        taskDatabaseService.removeTaskById(2)
        val tasksAfterDelete = taskDatabaseService.getTasks()
        assertArrayEquals(arrayOf("test3", "test1"), tasksAfterDelete.map { todo -> todo.name }.toTypedArray())
    }

    @Test
    fun updateElementNotExists() {
        taskDatabaseService.addTask("test", Date())
        val tasks = taskDatabaseService.getTasks()
        taskDatabaseService.updateTask(2, "test", Date())
        val tasksAfterUpdate = taskDatabaseService.getTasks()
        assertArrayEquals(tasks.toTypedArray(), tasksAfterUpdate.toTypedArray())
    }
}