package com.example.todolistssy.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistssy.domain.usecase.GetCompletedTodoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HistoryState(
    val completedTodoList: List<HistoryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class HistoryIntent {
    object LoadCompletedTodos : HistoryIntent()
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getCompletedTodoListUseCase: GetCompletedTodoListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        handleIntent(HistoryIntent.LoadCompletedTodos)
    }

    fun handleIntent(intent: HistoryIntent) {
        when (intent) {
            is HistoryIntent.LoadCompletedTodos -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    try {
                        getCompletedTodoListUseCase().collect { completedTodos ->
                            _state.update { 
                                it.copy(
                                    completedTodoList = HistoryMapper.toUiModelList(completedTodos),
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                    } catch (e: Exception) {
                        _state.update { 
                            it.copy(
                                isLoading = false,
                                error = e.message
                            )
                        }
                    }
                }
            }
        }
    }
} 