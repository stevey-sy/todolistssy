package com.example.todolistssy.presentation.history.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.todolistssy.R
import com.example.todolistssy.presentation.history.model.HistoryUiModel
import com.example.todolistssy.presentation.theme.TodoColors

@Composable
fun HistoryItem(
    todo: HistoryUiModel,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = TodoColors.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 등록일
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.created_date),
                    fontSize = 12.sp,
                    color = TodoColors.NormalGray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = todo.createdAt,
                    fontSize = 12.sp,
                    color = TodoColors.NormalGray
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 할 일 내용
            Text(
                text = todo.content,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TodoColors.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 완료일
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.completed_date),
                    fontSize = 12.sp,
                    color = TodoColors.Green,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = todo.completedAt,
                    fontSize = 12.sp,
                    color = TodoColors.Green
                )
            }
        }
    }
} 