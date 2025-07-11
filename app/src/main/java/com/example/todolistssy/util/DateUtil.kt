package com.example.todolistssy.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
    
    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }
    
    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }
    
    fun formatDateFromDate(date: Date): String {
        return dateFormat.format(date)
    }
    
    fun formatDateTimeFromDate(date: Date): String {
        return dateTimeFormat.format(date)
    }
} 