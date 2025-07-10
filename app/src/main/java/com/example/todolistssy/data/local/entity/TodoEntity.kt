package com.example.todolistssy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val encryptedContent: String,
    val contentIv: String,
    val createdAt: Long,
    val completedAt: Long = 0L
)
