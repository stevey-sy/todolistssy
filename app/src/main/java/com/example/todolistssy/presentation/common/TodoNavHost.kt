package com.example.todolistssy.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolistssy.R
import com.example.todolistssy.presentation.history.HistoryScreen
import com.example.todolistssy.presentation.history.HistoryViewModel
import com.example.todolistssy.presentation.home.HomeScreen
import com.example.todolistssy.presentation.home.HomeViewModel

@Composable
fun TodoNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    // strings.xml에서 route 이름들을 가져옴
    val routeHome = stringResource(R.string.route_home)
    val routeHistory = stringResource(R.string.route_history)
    
    NavHost(
        navController = navController,
        startDestination = routeHome,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(routeHome) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel
            )
        }
        composable(routeHistory) {
            val historyViewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(
                viewModel = historyViewModel
            )
        }
    }
} 