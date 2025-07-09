package com.example.todolistssy.domain.usecase

import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.data.repository.TodoRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(content: String) {
        val todo = Todo(
            content = content,
            createdAt = System.currentTimeMillis(),
            completedAt = 0L
        )
        todoRepository.insertTodo(todo)
    }
} 