package com.example.todolistssy.presentation.history.mapper

import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.presentation.history.model.HistoryUiModel
import com.example.todolistssy.util.DateUtil

object HistoryMapper {
    fun toUiModel(todo: Todo): HistoryUiModel =
        HistoryUiModel(
            id = todo.id,
            content = todo.content,
            createdAt = DateUtil.formatDate(todo.createdAt),
            completedAt = DateUtil.formatDate(todo.completedAt)
        )

    fun toUiModelList(todos: List<Todo>): List<HistoryUiModel> =
        todos.map { toUiModel(it) }
}