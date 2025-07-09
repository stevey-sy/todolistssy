package com.example.todolistssy.data.mapper

import com.example.todolistssy.data.local.entity.TodoEntity
import com.example.todolistssy.domain.data.Todo

class TodoMapper {
    
    fun mapToDomain(entity: TodoEntity): Todo {
        return Todo(
            id = entity.id,
            content = entity.content,
            createdAt = entity.createdAt,
            completedAt = entity.completedAt ?: 0L
        )
    }
    
    fun mapToEntity(domain: Todo): TodoEntity {
        return TodoEntity(
            id = domain.id,
            content = domain.content,
            createdAt = domain.createdAt,
            completedAt = if (domain.completedAt > 0L) domain.completedAt else null
        )
    }
    
    fun mapToDomainList(entityList: List<TodoEntity>): List<Todo> {
        return entityList.map { mapToDomain(it) }
    }
    
    fun mapToEntityList(domainList: List<Todo>): List<TodoEntity> {
        return domainList.map { mapToEntity(it) }
    }
}