package com.example.todolistssy.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolistssy.presentation.history.HistoryScreen
import com.example.todolistssy.presentation.history.HistoryViewModel
import com.example.todolistssy.presentation.home.HomeScreen
import com.example.todolistssy.presentation.home.HomeViewModel

@Composable
fun TodoNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("home") {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel
            )
        }
        composable("history") {
            val historyViewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(
                viewModel = historyViewModel
            )
        }
    }
} 