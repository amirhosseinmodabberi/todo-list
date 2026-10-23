package com.example.taskcloud.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskcloud.database.dao.TaskDao
import com.example.taskcloud.database.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 3,
)
@TypeConverters(
    LocalDateConverter::class,
)
abstract class ToDoListDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}