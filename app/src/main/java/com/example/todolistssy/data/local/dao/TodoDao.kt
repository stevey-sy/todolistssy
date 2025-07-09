package com.example.todolistssy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolistssy.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo WHERE completedAt IS NULL ORDER BY createdAt DESC")
    fun getUncompletedTodoList(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo WHERE completedAt IS NOT NULL ORDER BY completedAt DESC")
    fun getCompletedTodoList(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Query("DELETE FROM todo WHERE id = :id")
    suspend fun deleteTodo(id: Int)


}