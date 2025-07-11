package com.example.todolistssy.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.todolistssy.R
import com.example.todolistssy.presentation.history.component.HistoryItem
import com.example.todolistssy.presentation.history.component.LoadingBox
import com.example.todolistssy.presentation.history.component.ErrorBox
import com.example.todolistssy.presentation.history.component.EmptyBox
import com.example.todolistssy.presentation.theme.TodoColors

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TodoColors.LightGray)
            .padding(16.dp)
    ) {
        when {
            state.isLoading -> LoadingBox()
            state.error != null -> ErrorBox()
            state.completedTodoList.isEmpty() -> EmptyBox()
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.completedTodoList,
                        key = { todo -> todo.id }
                    ) { todo ->
                        HistoryItem(todo = todo)
                    }
                }
            }
        }
    }
} 