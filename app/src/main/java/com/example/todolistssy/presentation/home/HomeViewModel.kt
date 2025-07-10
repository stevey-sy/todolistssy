package com.example.todolistssy.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.domain.usecase.AddTodoUseCase
import com.example.todolistssy.domain.usecase.CompleteTodoUseCase
import com.example.todolistssy.domain.usecase.DeleteTodoUseCase
import com.example.todolistssy.domain.usecase.GetTodoListUseCase
import com.example.todolistssy.presentation.home.TodoUiModel
import com.example.todolistssy.presentation.home.TodoMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeState(
    val todoList: List<TodoUiModel> = emptyList(),
    val input: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class HomeIntent {
    data class InputChanged(val value: String) : HomeIntent()
    object AddTodo : HomeIntent()
    data class ToggleComplete(val id: Int) : HomeIntent()
    data class DeleteTodo(val id: Int) : HomeIntent()
    data class ShowDeleteButton(val id: Int) : HomeIntent()
    data class HideDeleteButton(val id: Int) : HomeIntent()
    object LoadTodos : HomeIntent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val completeTodoUseCase: CompleteTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        handleIntent(HomeIntent.LoadTodos)
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.InputChanged -> {
                _state.update { it.copy(input = intent.value) }
            }
            is HomeIntent.AddTodo -> {
                viewModelScope.launch {
                    if (_state.value.input.isNotBlank()) {
                        addTodoUseCase(_state.value.input)
                        _state.update { it.copy(input = "") }
                        // 모든 아이템의 삭제 모드를 초기화
                        _state.update { currentState ->
                            currentState.copy(
                                todoList = currentState.todoList.map { todo ->
                                    todo.copy(isDeleteMode = false)
                                }
                            )
                        }
                        handleIntent(HomeIntent.LoadTodos)
                    }
                }
            }
            is HomeIntent.ToggleComplete -> {
                viewModelScope.launch {
//                    completeTodoUseCase(intent.id)
//                    handleIntent(HomeIntent.LoadTodos)
                }
            }
            is HomeIntent.DeleteTodo -> {
                viewModelScope.launch {
                    deleteTodoUseCase(intent.id)
                    handleIntent(HomeIntent.LoadTodos)
                }
            }
            is HomeIntent.ShowDeleteButton -> {
                _state.update { currentState ->
                    currentState.copy(
                        todoList = currentState.todoList.map { todo ->
                            if (todo.id == intent.id) {
                                todo.copy(isDeleteMode = true)
                            } else {
                                todo
                            }
                        }
                    )
                }
            }
            is HomeIntent.HideDeleteButton -> {
                _state.update { currentState ->
                    currentState.copy(
                        todoList = currentState.todoList.map { todo ->
                            if (todo.id == intent.id) {
                                todo.copy(isDeleteMode = false)
                            } else {
                                todo
                            }
                        }
                    )
                }
            }
            is HomeIntent.LoadTodos -> {
                viewModelScope.launch {
                    getTodoListUseCase().collect { todos ->
                        val todoUiModels = TodoMapper.toUiModelList(todos).map {
                            it.copy(isDeleteMode = false)
                        }
                        _state.update { it.copy(todoList = todoUiModels) }
                    }
                }
            }
        }
    }
}