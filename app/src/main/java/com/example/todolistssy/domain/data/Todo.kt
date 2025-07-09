package com.example.todolistssy.domain.data

data class Todo(
    val id: Int = 0,
    val content: String,
    val createdAt: Long,
    val completedAt: Long
)

fun Todo.isCompleted(): Boolean = this.completedAt != 0L
