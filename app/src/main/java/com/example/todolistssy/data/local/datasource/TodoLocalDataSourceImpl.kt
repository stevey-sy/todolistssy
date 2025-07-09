package com.example.todolistssy.data.local.datasource

import com.example.todolistssy.data.local.entity.TodoEntity
import com.example.todolistssy.data.local.dao.TodoDao
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class TodoLocalDataSourceImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoLocalDataSource {
    override fun getUncompletedTodoList(): Flow<List<TodoEntity>> {
       return todoDao.getUncompletedTodoList()
    }

    override fun getCompletedTodoList(): Flow<List<TodoEntity>> {
        return todoDao.getCompletedTodoList()
    }

    override suspend fun updateTodo(todo: TodoEntity) {
        return todoDao.updateTodo(todo)
    }

    override suspend fun insertTodo(todo: TodoEntity) {
        return todoDao.insertTodo(todo)
    }

    override suspend fun deleteTodo(id: Int) {
        return todoDao.deleteTodo(id)
    }
}