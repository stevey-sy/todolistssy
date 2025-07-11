package com.example.todolistssy.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity

fun Modifier.advancedImePadding() = composed {
    var consumePadding by remember { mutableStateOf(0) }
    onGloballyPositioned { coordinates ->
        consumePadding = coordinates.findRootCoordinates().size.height - 
            (coordinates.positionInWindow().y + coordinates.size.height).toInt().coerceAtLeast(0)
    }
    .consumeWindowInsets(
        PaddingValues(bottom = with(LocalDensity.current) { consumePadding.toDp() })
    )
    .imePadding()
} 