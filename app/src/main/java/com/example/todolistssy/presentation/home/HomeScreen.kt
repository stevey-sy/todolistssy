package com.example.todolistssy.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.todolistssy.domain.data.Todo

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F8))
    ) {
        // 타이틀
        Text(
            text = "DEEP.FINE TODO",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 24.dp)
        )

        // 할 일 목록
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            items(state.todoList) { todo ->
                TodoItem(
                    todo = todo,
                    onCheckedChange = { viewModel.handleIntent(HomeIntent.ToggleComplete(todo.id)) },
                    onDelete = { viewModel.handleIntent(HomeIntent.DeleteTodo(todo.id)) }
                )
            }
        }

        // 입력창 + 추가 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = state.input,
                onValueChange = { viewModel.handleIntent(HomeIntent.InputChanged(it)) },
                placeholder = { Text("할 일을 입력해주세요.") },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { viewModel.handleIntent(HomeIntent.AddTodo) },
                enabled = state.input.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "추가"
                )
            }
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onCheckedChange() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = todo.content,
                modifier = Modifier.weight(1f)
            )
        }
        if (!todo.isCompleted) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("삭제", color = Color.White)
                }
            }
        }
    }
}