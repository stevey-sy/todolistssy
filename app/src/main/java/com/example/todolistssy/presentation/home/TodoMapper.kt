package com.example.todolistssy.presentation.home

import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.domain.data.isCompleted

object TodoMapper {
    fun toUiModel(todo: Todo): TodoUiModel =
        TodoUiModel(
            id = todo.id,
            content = todo.content,
            isCompleted = todo.isCompleted()
        )

    fun toUiModelList(todos: List<Todo>): List<TodoUiModel> =
        todos.map { toUiModel(it) }
} 