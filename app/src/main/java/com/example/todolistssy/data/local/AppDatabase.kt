package com.example.todolistssy.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.todolistssy.data.local.dao.TodoDao
import com.example.todolistssy.data.local.entity.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}