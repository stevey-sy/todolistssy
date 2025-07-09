package com.example.todolistssy.di

import android.content.Context
import androidx.room.Room
import com.example.todolistssy.data.local.AppDatabase
import com.example.todolistssy.data.local.dao.TodoDao
import com.example.todolistssy.data.local.datasource.TodoLocalDataSource
import com.example.todolistssy.data.local.datasource.TodoLocalDataSourceImpl
import com.example.todolistssy.data.mapper.TodoMapper
import com.example.todolistssy.data.repository.TodoRepository
import com.example.todolistssy.data.repository.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: AppDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideTodoLocalDataSource(
        todoDao: TodoDao
    ): TodoLocalDataSource {
        return TodoLocalDataSourceImpl(todoDao)
    }

    @Provides
    @Singleton
    fun provideTodoMapper(): TodoMapper {
        return TodoMapper()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(
        todoLocalDataSource: TodoLocalDataSource,
        todoMapper: TodoMapper
    ): TodoRepository {
        return TodoRepositoryImpl(todoLocalDataSource, todoMapper)
    }
} 