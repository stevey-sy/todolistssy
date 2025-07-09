package com.example.todolistssy.di

import com.example.todolistssy.data.repository.TodoRepository
import com.example.todolistssy.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetTodoListUseCase(
        todoRepository: TodoRepository
    ): GetTodoListUseCase {
        return GetTodoListUseCase(todoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCompletedTodoListUseCase(
        todoRepository: TodoRepository
    ): GetCompletedTodoListUseCase {
        return GetCompletedTodoListUseCase(todoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideAddTodoUseCase(
        todoRepository: TodoRepository
    ): AddTodoUseCase {
        return AddTodoUseCase(todoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCompleteTodoUseCase(
        todoRepository: TodoRepository
    ): CompleteTodoUseCase {
        return CompleteTodoUseCase(todoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteTodoUseCase(
        todoRepository: TodoRepository
    ): DeleteTodoUseCase {
        return DeleteTodoUseCase(todoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateTodoContentUseCase(
        todoRepository: TodoRepository
    ): UpdateTodoContentUseCase {
        return UpdateTodoContentUseCase(todoRepository)
    }
} 