package com.example.todolistssy.data.local.datasource

import com.example.todolistssy.domain.data.Todo
import kotlinx.coroutines.flow.Flow

interface TodoLocalDataSource {
    suspend fun addTodo(content: String)
    suspend fun deleteTodo(id: Int)
    suspend fun completeTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    fun getTodos(): Flow<List<Todo>>
    fun getCompletedTodos(): Flow<List<Todo>>
}