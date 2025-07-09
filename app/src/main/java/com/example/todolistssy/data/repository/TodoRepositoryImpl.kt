package com.example.todolistssy.data.repository


import com.example.todolistssy.data.local.datasource.TodoLocalDataSource
import com.example.todolistssy.data.mapper.TodoMapper
import com.example.todolistssy.domain.data.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import jakarta.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoLocalDataSource: TodoLocalDataSource,
    private val todoMapper: TodoMapper
) : TodoRepository {
    
    override fun getUncompletedTodoList(): Flow<List<Todo>> {
        return todoLocalDataSource.getUncompletedTodoList()
            .map { entityList -> todoMapper.mapToDomainList(entityList) }
    }
    
    override fun getCompletedTodoList(): Flow<List<Todo>> {
        return todoLocalDataSource.getCompletedTodoList()
            .map { entityList -> todoMapper.mapToDomainList(entityList) }
    }
    
    override suspend fun insertTodo(todo: Todo) {
        val entity = todoMapper.mapToEntity(todo)
        todoLocalDataSource.insertTodo(entity)
    }
    
    override suspend fun updateTodo(todo: Todo) {
        val entity = todoMapper.mapToEntity(todo)
        todoLocalDataSource.updateTodo(entity)
    }
    
    override suspend fun deleteDodoById(id: Int) {
        todoLocalDataSource.deleteTodo(id)
    }
}