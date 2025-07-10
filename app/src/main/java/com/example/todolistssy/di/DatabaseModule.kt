package com.example.todolistssy.di

import android.content.Context
import androidx.room.Room
import com.example.todolistssy.data.local.AppDatabase
import com.example.todolistssy.data.local.CryptoManager
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    fun provideTodoDao(database: AppDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager {
        return CryptoManager()
    }

    @Provides
    @Singleton
    fun provideTodoMapper(cryptoManager: CryptoManager): TodoMapper {
        return TodoMapper(cryptoManager)
    }

    @Provides
    @Singleton
    fun provideTodoLocalDataSource(
        todoDao: TodoDao,
        todoMapper: TodoMapper
    ): TodoLocalDataSource {
        return TodoLocalDataSourceImpl(todoDao, todoMapper)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(
        todoLocalDataSource: TodoLocalDataSource
    ): TodoRepository {
        return TodoRepositoryImpl(todoLocalDataSource)
    }
} 