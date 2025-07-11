package com.example.todolistssy.presentation.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolistssy.presentation.home.model.TodoUiModel

@Composable
fun TodoListView(
    todoList: List<TodoUiModel>,
    listState: LazyListState,
    onCheckedChange: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onShowDeleteButton: (Int) -> Unit,
    onHideDeleteButton: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(
            items = todoList,
            key = { todo -> todo.id }
        ) { todo ->
            TodoItem(
                todo = todo,
                onCheckedChange = { onCheckedChange(todo.id) },
                onDelete = { onDelete(todo.id) },
                onShowDeleteButton = { onShowDeleteButton(todo.id) },
                onHideDeleteButton = { onHideDeleteButton(todo.id) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
} 