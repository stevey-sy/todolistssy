package com.example.todolistssy.data.mapper

import com.example.todolistssy.util.CryptoManager
import com.example.todolistssy.util.EncryptedData
import com.example.todolistssy.data.local.entity.TodoEntity
import com.example.todolistssy.domain.data.Todo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoMapper @Inject constructor(
    private val cryptoManager: CryptoManager
) {
    
    /**
     * TodoEntity -> Todo (복호화)
     */
    fun mapToDomain(entity: TodoEntity): Todo {
        val decryptedContent = cryptoManager.decrypt(
            EncryptedData(
                encryptedText = entity.encryptedContent,
                iv = entity.contentIv
            )
        )
        
        return Todo(
            id = entity.id,
            content = decryptedContent,
            createdAt = entity.createdAt,
            completedAt = entity.completedAt
        )
    }
    
    /**
     * Todo -> TodoEntity (암호화)
     */
    fun mapToEntity(domain: Todo): TodoEntity {
        val encryptedData = cryptoManager.encrypt(domain.content)
        
        return TodoEntity(
            id = domain.id,
            encryptedContent = encryptedData.encryptedText,
            contentIv = encryptedData.iv,
            createdAt = domain.createdAt,
            completedAt = domain.completedAt
        )
    }
    
    /**
     * 새로운 Todo 생성용 (ID 없이)
     */
    fun mapToEntityForInsert(content: String, createdAt: Long = System.currentTimeMillis()): TodoEntity {
        val encryptedData = cryptoManager.encrypt(content)
        
        return TodoEntity(
            encryptedContent = encryptedData.encryptedText,
            contentIv = encryptedData.iv,
            createdAt = createdAt,
            completedAt = 0L
        )
    }
    
    /**
     * List 변환 메서드들
     */
    fun mapToDomainList(entityList: List<TodoEntity>): List<Todo> {
        return entityList.map { mapToDomain(it) }
    }
    
    fun mapToEntityList(domainList: List<Todo>): List<TodoEntity> {
        return domainList.map { mapToEntity(it) }
    }
} 