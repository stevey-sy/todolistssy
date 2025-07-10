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
import kotlinx.coroutines.delay

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
                    // 현재 TodoUiModel 찾기
                    val currentTodo = _state.value.todoList.find { it.id == intent.id }
                    if (currentTodo != null) {
                        // 1. UI 상태 즉시 업데이트 (체크박스 채우기)
                        _state.update { currentState ->
                            currentState.copy(
                                todoList = currentState.todoList.map { todo ->
                                    if (todo.id == intent.id) {
                                        todo.copy(isCompleted = !todo.isCompleted)
                                    } else {
                                        todo
                                    }
                                }
                            )
                        }
                        
                        // 2. 체크박스 애니메이션이 완료될 때까지 대기
                        delay(600)
                        
                        // 3. 데이터베이스 업데이트
                        val updatedTodo = TodoMapper.toToggleCompleteTodo(currentTodo)
                        completeTodoUseCase(updatedTodo)
                        
                        // 4. 완료된 아이템이면 슬라이드 아웃 상태로 변경
                        if (!currentTodo.isCompleted) { // 체크하는 경우 (미완료 -> 완료)
                            _state.update { currentState ->
                                currentState.copy(
                                    todoList = currentState.todoList.map { todo ->
                                        if (todo.id == intent.id) {
                                            todo.copy(isSlideOut = true)
                                        } else {
                                            todo
                                        }
                                    }
                                )
                            }
                            
                            // 5. 슬라이드 애니메이션 완료 후 아이템 제거
                            delay(500)
                            _state.update { currentState ->
                                currentState.copy(
                                    todoList = currentState.todoList.filter { it.id != intent.id }
                                )
                            }
                        }
                    }
                }
            }
            is HomeIntent.DeleteTodo -> {
                viewModelScope.launch {
                    deleteTodoUseCase(intent.id)
                    // LoadTodos 대신 직접 상태에서 삭제된 아이템 제거
                    _state.update { currentState ->
                        currentState.copy(
                            todoList = currentState.todoList.filter { it.id != intent.id }
                        )
                    }
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
                        val currentDeleteModes = _state.value.todoList.associate { it.id to it.isDeleteMode }
                        val todoUiModels = TodoMapper.toUiModelList(todos).map { newTodo ->
                            newTodo.copy(isDeleteMode = currentDeleteModes[newTodo.id] == true)
                        }
                        _state.update { it.copy(todoList = todoUiModels) }
                    }
                }
            }
        }
    }
}