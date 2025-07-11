package com.example.todolistssy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todolistssy.presentation.common.TodoAppBar
import com.example.todolistssy.presentation.common.TodoNavHost
import com.example.todolistssy.presentation.theme.TodolistssyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodolistssyTheme {
                TodoApp()
            }
        }
    }
}

@Composable
fun TodoApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // 시스템 바 색상을 검정색으로 설정
    val view = LocalView.current
    SideEffect {
        val window = (view.context as ComponentActivity).window
        window.statusBarColor = android.graphics.Color.BLACK
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TodoAppBar(
                currentDestination = currentDestination,
                navController = navController
            )
        }
    ) { innerPadding ->
        TodoNavHost(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}