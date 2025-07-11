package com.example.todolistssy.presentation.history.model

data class HistoryUiModel(
    val id: Int,
    val content: String,
    val createdAt: String, // 포맷된 등록일
    val completedAt: String // 포맷된 완료일
)