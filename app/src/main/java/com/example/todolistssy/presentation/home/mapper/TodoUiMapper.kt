package com.example.todolistssy.presentation.home.mapper

import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.domain.data.isCompleted
import com.example.todolistssy.presentation.home.model.TodoUiModel

object TodoUiMapper {
    fun toUiModel(todo: Todo): TodoUiModel {
        return TodoUiModel(
            id = todo.id.toInt(),
            content = todo.content,
            isCompleted = todo.isCompleted(),
            isDeleteMode = false,
        )
    }

    fun toUiModelList(todos: List<Todo>): List<TodoUiModel> {
        return todos.map { toUiModel(it) }
    }

    fun toToggleCompleteTodo(todoUiModel: TodoUiModel): Todo {
        return Todo(
            id = todoUiModel.id,
            content = todoUiModel.content,
            createdAt = System.currentTimeMillis(),
            completedAt = if (!todoUiModel.isCompleted) System.currentTimeMillis() else 0L
        )
    }
}