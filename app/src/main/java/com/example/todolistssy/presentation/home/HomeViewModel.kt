package com.example.todolistssy.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistssy.domain.usecase.AddTodoUseCase
import com.example.todolistssy.domain.usecase.CompleteTodoUseCase
import com.example.todolistssy.domain.usecase.DeleteTodoUseCase
import com.example.todolistssy.domain.usecase.GetTodoListUseCase
import com.example.todolistssy.presentation.home.mapper.TodoUiMapper
import com.example.todolistssy.presentation.home.model.TodoUiModel
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
                        // UI 상태 업데이트
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
                        // 0.5초 딜레이 후 데이터베이스 로직 실행
                        delay(500)
                        // 데이터베이스 업데이트
                        val updatedTodo = TodoUiMapper.toToggleCompleteTodo(currentTodo)
                        completeTodoUseCase(updatedTodo)
                        
                        // 완료된 아이템이면 리스트에서 제거, 미완료면 상태 업데이트
                        if (!currentTodo.isCompleted) { // 체크하는 경우 (미완료 -> 완료)
                            // 완료된 아이템은 리스트에서 제거
                            _state.update { currentState ->
                                currentState.copy(
                                    todoList = currentState.todoList.filter { it.id != intent.id }
                                )
                            }
                        } else { // 체크 해제하는 경우 (완료 -> 미완료)
                            // 미완료로 변경하고 다시 로드
                            handleIntent(HomeIntent.LoadTodos)
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
                        val todoUiModels = TodoUiMapper.toUiModelList(todos).map { newTodo ->
                            newTodo.copy(isDeleteMode = currentDeleteModes[newTodo.id] == true)
                        }
                        _state.update { it.copy(todoList = todoUiModels) }
                    }
                }
            }
            // 애니메이션 관련 Intent 제거
        }
    }
}