package com.example.todolistssy.data.local.datasource

import com.example.todolistssy.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoLocalDataSource {

    fun getUncompletedTodoList(): Flow<List<TodoEntity>>

    fun getCompletedTodoList(): Flow<List<TodoEntity>>

    suspend fun updateTodo(todo: TodoEntity)

    suspend fun insertTodo(todo: TodoEntity)

    suspend fun deleteTodo(id: Int)
}