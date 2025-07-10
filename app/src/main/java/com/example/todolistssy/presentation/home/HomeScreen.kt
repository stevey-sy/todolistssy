package com.example.todolistssy.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.todolistssy.domain.data.Todo
import com.example.todolistssy.presentation.home.TodoUiModel
import com.example.todolistssy.ui.theme.TodoColors
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput

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
            .background(Color(0xFFF6F6F8))
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
                    placeholder = { Text("할 일을 입력해주세요.", color = Color(0xFF71797E)) },
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
                        contentDescription = "추가",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItem(
    todo: TodoUiModel,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit,
    onShowDeleteButton: () -> Unit,
    onHideDeleteButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    var totalDragAmount by remember(todo.id) { mutableStateOf(0f) }
    
    val fadeAlpha by animateFloatAsState(
        targetValue = if (todo.isSlideOut) 0f else 1f,
        animationSpec = tween(500),
        label = "fadeAlpha"
    )
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(fadeAlpha)
            .background(Color.White)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        totalDragAmount = 0f
                    },
                    onDragEnd = {
                        // 스와이프 방향에 따라 처리
                        if (totalDragAmount < -50f) {
                            // 좌측 스와이프 (음수) - 삭제 버튼 보이기
                            onShowDeleteButton()
                        } else if (totalDragAmount > 50f) {
                            // 우측 스와이프 (양수) - 삭제 버튼 숨기기
                            onHideDeleteButton()
                        }
                        totalDragAmount = 0f
                    }
                ) { _, dragAmount ->
                    totalDragAmount += dragAmount
                }
            }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 커스텀 원형 체크박스
            val checkboxBackgroundColor by animateColorAsState(
                targetValue = if (todo.isCompleted) TodoColors.Orange else Color.Transparent,
                animationSpec = tween(400),
                label = "checkboxBackground"
            )
            val checkboxBorderColor by animateColorAsState(
                targetValue = if (todo.isCompleted) TodoColors.Orange else Color.Gray,
                animationSpec = tween(400),
                label = "checkboxBorder"
            )
            val checkboxScale by animateFloatAsState(
                targetValue = if (todo.isCompleted) 1.1f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "checkboxScale"
            )
            
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .scale(checkboxScale)
                    .clip(CircleShape)
                    .background(checkboxBackgroundColor)
                    .border(
                        width = 2.dp,
                        color = checkboxBorderColor,
                        shape = CircleShape
                    )
                    .clickable { onCheckedChange() },
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = todo.isCompleted,
                    enter = scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn(animationSpec = tween(300)),
                    exit = scaleOut(animationSpec = tween(150)) + fadeOut(animationSpec = tween(150))
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "완료",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            
            val textColor by animateColorAsState(
                targetValue = if (todo.isCompleted) Color.Gray else Color.Black,
                animationSpec = tween(400),
                label = "textColor"
            )
            
            Text(
                text = todo.content,
                modifier = Modifier.weight(1f),
                color = textColor,
                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
            
            // 삭제 버튼 - isDeleteMode에 따라 visibility 조절
            androidx.compose.animation.AnimatedVisibility(
                visible = todo.isDeleteMode,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            ) {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("삭제", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}