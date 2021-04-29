package com.pack.todolist.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val date: Int
)
{
    constructor(id: Int) : this(id, "", 0)
    constructor(name : String, date: Int) : this(0, name, date)
}