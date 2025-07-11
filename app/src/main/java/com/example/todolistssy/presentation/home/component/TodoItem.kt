package com.example.todolistssy.presentation.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.todolistssy.R
import com.example.todolistssy.presentation.home.model.TodoUiModel
import com.example.todolistssy.presentation.theme.TodoColors


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
    
    Column(
        modifier = modifier
            .fillMaxWidth()
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
                        contentDescription = stringResource(R.string.complete_todo),
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
            
            // 삭제 버튼 영역 - 항상 공간 확보
            val deleteButtonAlpha by animateFloatAsState(
                targetValue = if (todo.isDeleteMode) 1f else 0f,
                animationSpec = tween(300),
                label = "deleteButtonAlpha"
            )
            
            Row(
                modifier = Modifier.width(80.dp), // 삭제 버튼 영역 고정 크기
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDelete,
                    enabled = todo.isDeleteMode,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.alpha(deleteButtonAlpha)
                ) {
                    Text(stringResource(R.string.delete_todo), color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
} 