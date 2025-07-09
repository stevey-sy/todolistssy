package com.example.todolistssy.domain.usecase


import com.example.todolistssy.data.repository.TodoRepository
import com.example.todolistssy.domain.data.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    operator fun invoke(): Flow<List<Todo>> {
        return todoRepository.getUncompletedTodoList()
    }
}