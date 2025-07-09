package com.example.todolistssy.domain.usecase

import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.data.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCompletedTodoListUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    operator fun invoke(): Flow<List<Todo>> {
        return todoRepository.getCompletedTodoList()
    }
} 