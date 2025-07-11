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

/**
 * 키보드가 올라올 때 발생하는 중복 패딩 문제를 해결하기 위한 Modifier 확장 함수
 */
fun Modifier.advancedImePadding() = composed {
    // 동적으로 계산될 패딩 값을 저장하는 상태
    // 컴포넌트 위치가 변경될 때마다 업데이트 됨
    var consumePadding by remember { mutableStateOf(0) }
    
    // 컴포넌트의 위치가 변경될 때마다 실행되는 콜백
    onGloballyPositioned { coordinates ->
        // 패딩 계산 공식: 전체 화면 높이 - 컴포넌트 하단 위치
        consumePadding = coordinates.findRootCoordinates().size.height - // 전체 화면 높이 (px)
            (coordinates.positionInWindow().y + coordinates.size.height) // 컴포넌트 하단 위치 (px)
                .toInt().coerceAtLeast(0) // 음수 방지 (최소값 0)
        
        // 결과: 컴포넌트 하단부터 화면 끝까지의 거리
        // 이 값이 키보드에 의해 가려질 수 있는 영역의 높이
    }
    .consumeWindowInsets(
        // 계산된 패딩 값을 WindowInsets로 변환하여 소비
        // 이를 통해 중복 패딩을 방지하고 정확한 insets 처리
        PaddingValues(bottom = with(LocalDensity.current) { consumePadding.toDp() })
    )
    .imePadding() // 마지막에 기본 IME 패딩 적용 (시스템 키보드 호환성 유지)
} 