package com.example.todolistssy.presentation.home.model

data class TodoUiModel(
    val id: Int,
    val content: String,
    val isCompleted: Boolean,
    val isDeleteMode: Boolean = false,
)