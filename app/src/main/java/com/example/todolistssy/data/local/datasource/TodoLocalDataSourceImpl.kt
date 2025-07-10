package com.example.todolistssy.data.local.datasource

import com.example.todolistssy.data.local.dao.TodoDao
import com.example.todolistssy.data.mapper.TodoMapper
import com.example.todolistssy.domain.data.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoLocalDataSourceImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val todoMapper: TodoMapper
) : TodoLocalDataSource {

    override suspend fun addTodo(content: String) {
        val todoEntity = todoMapper.mapToEntityForInsert(content)
        todoDao.insert(todoEntity)
    }

    override suspend fun deleteTodo(id: Int) {
        todoDao.deleteById(id)
    }

    override suspend fun completeTodo(todo: Todo) {
        val todoEntity = todoMapper.mapToEntity(todo)
        todoDao.update(todoEntity)
    }

    override suspend fun updateTodo(todo: Todo) {
        val todoEntity = todoMapper.mapToEntity(todo)
        todoDao.update(todoEntity)
    }

    override fun getTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos().map { entities ->
            todoMapper.mapToDomainList(entities)
        }
    }

    override fun getCompletedTodos(): Flow<List<Todo>> {
        return todoDao.getCompletedTodos().map { entities ->
            todoMapper.mapToDomainList(entities)
        }
    }
}