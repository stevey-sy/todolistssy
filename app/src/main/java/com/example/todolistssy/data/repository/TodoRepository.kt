package com.example.todolistssy.data.repository


import com.example.todolistssy.domain.data.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getUncompletedTodoList(): Flow<List<Todo>>
    fun getCompletedTodoList(): Flow<List<Todo>>
    suspend fun insertTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteDodoById(id: Int)
}