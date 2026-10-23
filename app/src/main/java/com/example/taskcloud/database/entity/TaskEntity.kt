package com.example.taskcloud.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskcloud.model.Task
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val task: String,
    val isCompleted: Boolean,
    val dueDate: LocalDate?,
    val dueHour: Int? = null,
    val dueMinute: Int? = null,
)

fun TaskEntity.mapToTask() = Task(
    id = id,
    task = task,
    isCompleted = isCompleted,
    dueDate = dueDate,
    dueHour = dueHour,
    dueMinute = dueMinute
)
