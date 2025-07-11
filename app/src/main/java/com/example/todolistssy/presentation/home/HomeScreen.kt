package com.example.todolistssy.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import com.example.todolistssy.presentation.home.component.TodoListView
import com.example.todolistssy.presentation.home.component.TextFieldView
import com.example.todolistssy.presentation.theme.TodoColors

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // 아이템이 추가될 때 스크롤을 맨 위로
    LaunchedEffect(state.todoList.size) {
        if (state.todoList.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TodoColors.LightGray)
    ) {
        // 할 일 목록
        TodoListView(
            todoList = state.todoList,
            listState = listState,
            onCheckedChange = { todoId -> viewModel.handleIntent(HomeIntent.ToggleComplete(todoId)) },
            onDelete = { todoId -> viewModel.handleIntent(HomeIntent.DeleteTodo(todoId)) },
            onShowDeleteButton = { todoId -> viewModel.handleIntent(HomeIntent.ShowDeleteButton(todoId)) },
            onHideDeleteButton = { todoId -> viewModel.handleIntent(HomeIntent.HideDeleteButton(todoId)) },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp, horizontal = 10.dp)
        )

        // 입력창 + 추가 버튼
        TextFieldView(
            input = state.input,
            onInputChanged = { viewModel.handleIntent(HomeIntent.InputChanged(it)) },
            onAddTodo = { viewModel.handleIntent(HomeIntent.AddTodo) }
        )
    }
}