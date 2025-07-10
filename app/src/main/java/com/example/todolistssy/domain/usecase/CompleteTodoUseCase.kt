package com.example.todolistssy.domain.usecase

import com.example.todolistssy.data.repository.TodoRepository
import com.example.todolistssy.domain.data.Todo
import javax.inject.Inject

class CompleteTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo) {
        repository.completeTodo(todo)
    }
} 