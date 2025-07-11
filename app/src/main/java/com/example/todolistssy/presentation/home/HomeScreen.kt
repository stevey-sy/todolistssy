package com.example.todolistssy.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.example.todolistssy.R
import com.example.todolistssy.presentation.home.component.TodoItem
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
            .imePadding()
            .padding(bottom = 12.dp)
            .background(TodoColors.LightGray)
    ) {
        // 할 일 목록
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            items(
                items = state.todoList,
                key = { todo -> todo.id }
            ) { todo ->
                TodoItem(
                    todo = todo,
                    onCheckedChange = { viewModel.handleIntent(HomeIntent.ToggleComplete(todo.id)) },
                    onDelete = { viewModel.handleIntent(HomeIntent.DeleteTodo(todo.id)) },
                    onShowDeleteButton = { viewModel.handleIntent(HomeIntent.ShowDeleteButton(todo.id)) },
                    onHideDeleteButton = { viewModel.handleIntent(HomeIntent.HideDeleteButton(todo.id)) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        // 입력창 + 추가 버튼
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = state.input,
                    onValueChange = { viewModel.handleIntent(HomeIntent.InputChanged(it)) },
                    placeholder = { Text(stringResource(R.string.todo_input_placeholder), color = Color(0xFF71797E)) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (state.input.isNotBlank()) {
                                viewModel.handleIntent(HomeIntent.AddTodo)
                            }
                        }
                    )
                )
                IconButton(
                    onClick = { viewModel.handleIntent(HomeIntent.AddTodo) },
                    enabled = state.input.isNotBlank(),
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .background(
                            if (state.input.isNotBlank()) Color.Black else TodoColors.StealGray
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check, // 검색 아이콘을 원하면 Icons.Default.Search로 변경
                        contentDescription = stringResource(R.string.add_todo),
                        tint = Color.White
                    )
                }
            }
        }
    }
}