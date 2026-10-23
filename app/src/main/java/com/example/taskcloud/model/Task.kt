package com.example.taskcloud.model

import com.example.taskcloud.database.entity.TaskEntity
import java.time.LocalDate

data class Task(
    val id: Long? = null,
    val task: String,
    val isCompleted: Boolean = false,
    val dueDate: LocalDate? = null,
    val dueHour: Int? = null,
    val dueMinute: Int? = null,
)

fun Task.mapToEntity() = TaskEntity(
    id = id ?: 0,
    task = task,
    isCompleted = isCompleted,
    dueDate = dueDate,
    dueHour = dueHour,
    dueMinute = dueMinute
)
