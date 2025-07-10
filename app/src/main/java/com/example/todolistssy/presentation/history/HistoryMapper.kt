package com.example.todolistssy.presentation.history

import com.example.todolistssy.domain.data.Todo
import java.text.SimpleDateFormat
import java.util.*

object HistoryMapper {
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    
    fun toUiModel(todo: Todo): HistoryUiModel =
        HistoryUiModel(
            id = todo.id,
            content = todo.content,
            createdAt = dateFormat.format(Date(todo.createdAt)),
            completedAt = dateFormat.format(Date(todo.completedAt))
        )

    fun toUiModelList(todos: List<Todo>): List<HistoryUiModel> =
        todos.map { toUiModel(it) }
} 