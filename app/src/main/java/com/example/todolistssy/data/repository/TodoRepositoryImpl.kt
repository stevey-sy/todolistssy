package com.example.todolistssy.data.repository

import com.example.todolistssy.data.local.datasource.TodoLocalDataSource
import com.example.todolistssy.domain.data.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoLocalDataSource: TodoLocalDataSource
) : TodoRepository {

    override suspend fun addTodo(content: String) {
        todoLocalDataSource.addTodo(content)
    }

    override suspend fun deleteTodo(id: Int) {
        todoLocalDataSource.deleteTodo(id)
    }

    override suspend fun completeTodo(todo: Todo) {
        todoLocalDataSource.completeTodo(todo)
    }

    override suspend fun updateTodo(todo: Todo) {
        todoLocalDataSource.updateTodo(todo)
    }

    override fun getTodos(): Flow<List<Todo>> {
        return todoLocalDataSource.getTodos()
    }

    override fun getCompletedTodos(): Flow<List<Todo>> {
        return todoLocalDataSource.getCompletedTodos()
    }
}