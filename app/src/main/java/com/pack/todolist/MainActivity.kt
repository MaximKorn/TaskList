package com.pack.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.pack.todolist.database.TLDatabase
import com.pack.todolist.database.Task
import com.pack.todolist.database.TaskDao
import com.pack.todolist.database.TaskDatabaseService
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var taskDatabaseService: TaskDatabaseService
    private lateinit var mainView: ConstraintLayout
    private lateinit var editTask: EditText
    private lateinit var cancelButton: TextView
    private lateinit var saveButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainView = findViewById(R.id.activity_main)
        progressBar = findViewById(R.id.progressBar)

        val db = Room.databaseBuilder(
                applicationContext,
                TLDatabase::class.java, "db_task_list"
        ).allowMainThreadQueries().build()
        val dao: TaskDao = db.taskDao ?: return
        taskDatabaseService = TaskDatabaseService(dao)

        updateTaskList()

    }


    fun openPopUpForAdd(view: View) {
        openPopUpForAdd(view, null)
    }

    private fun openPopUpForAdd(view: View, task: Task?) {
        val popupView = layoutInflater.inflate(R.layout.add_popup, null)

        val popupWindow = PopupWindow(
                view.context
        )

        popupWindow.contentView = popupView
        popupWindow.contentView.measure(popupWindow.width, popupWindow.height)

        editTask = popupView.findViewById(R.id.editTask)
        cancelButton = popupView.findViewById(R.id.button_cancel)
        saveButton = popupView.findViewById(R.id.button_save)

        if (task != null) {
            editTask.text.append(task.name)
        }
        editTask.addTextChangedListener {
            saveButton.isEnabled = editTask.text.toString().isNotEmpty()
        }

        cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

        saveButton.setOnClickListener {

            progressBar.visibility = View.VISIBLE

            when {
                task == null -> {
                    taskDatabaseService.addTask(editTask.text.toString(), Date())
                    updateTaskList()
                    popupWindow.dismiss()
                }
                task.name == editTask.text.toString() -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.task_not_changed_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    taskDatabaseService.updateTask(task.id, editTask.text.toString(), Date())
                    updateTaskList()
                    popupWindow.dismiss()
                }
            }

            progressBar.visibility = View.GONE
        }

        popupWindow.showAsDropDown(mainView, 160, 600, Gravity.CENTER)
        popupWindow.isFocusable = true;
        popupWindow.update();
    }

    private fun openPopUpForConfirm(id: Int) {
        val popupView = layoutInflater.inflate(R.layout.confirm_popup, null)

        val popupWindow = PopupWindow(
                mainView.context
        )

        popupWindow.contentView = popupView
        popupWindow.contentView.measure(popupWindow.width, popupWindow.height)

        val buttonCancel = popupView.findViewById<TextView>(R.id.button_no)
        val buttonSave = popupView.findViewById<Button>(R.id.button_yes)

        buttonCancel.setOnClickListener {
            popupWindow.dismiss()
        }

        buttonSave.setOnClickListener {
            try {
                taskDatabaseService.removeTaskById(id)
                updateTaskList()
                popupWindow.dismiss()
            } catch (e: Exception) {
                Toast.makeText(
                        applicationContext,
                        getString(R.string.task_can_not_be_saved_message),
                        Toast.LENGTH_SHORT
                ).show()
            }
        }

        popupWindow.showAsDropDown(mainView, 90, 600, Gravity.START)
        popupWindow.isFocusable = true;
        popupWindow.update();
    }

    private fun updateTaskList() {

        progressBar.visibility = View.VISIBLE

        val tasks = taskDatabaseService.getTasks()

        tableLayout = findViewById(R.id.table_layout)

        tableLayout.removeAllViews()

        if (tasks.isEmpty()) {
            val tableRow = TableRow(this)
            val textView = TextView(this)
            val text = getString(R.string.task_list_empty)
            textView.text = text
            tableRow.addView(textView)
            tableLayout.addView(tableRow)
        }

        for (i in 1..tasks.size + 1) {
            val tableRow = TableRow(this)
            val textView = TextView(this)
            val text = "$i. ${tasks[i - 1].name}"
            textView.text = text
            tableRow.addView(textView)
            val editButton = Button(this)
            editButton.text = getString(R.string.edit_button)
            editButton.setOnClickListener {
                openPopUpForAdd(findViewById<ConstraintLayout>(R.id.activity_main), tasks[i - 1])
            }
            tableRow.addView(editButton)
            val deleteButton = Button(this)
            deleteButton.text = getString(R.string.delete_button)
            deleteButton.setOnClickListener {
                openPopUpForConfirm(tasks[i - 1].id)
                updateTaskList()
            }
            tableRow.addView(deleteButton)
            tableLayout.addView(tableRow)
        }
        progressBar.visibility = View.GONE
    }
}