package com.example.todolistssy.domain.usecase

import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.data.repository.TodoRepository
import javax.inject.Inject

class UpdateTodoContentUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo, newContent: String) {
        val updatedTodo = todo.copy(content = newContent)
        todoRepository.updateTodo(updatedTodo)
    }
} 