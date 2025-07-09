package com.example.todolistssy.domain.usecase

import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.data.repository.TodoRepository
import javax.inject.Inject

class CompleteTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo) {
        val completedTodo = todo.copy(
            completedAt = System.currentTimeMillis()
        )
        todoRepository.updateTodo(completedTodo)
    }
} 