package com.example.todolistssy.domain.usecase

import com.example.todolistssy.data.repository.TodoRepository
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteTodo(id)
    }
} 