package com.example.todolistssy.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.todolistssy.R
import com.example.todolistssy.presentation.theme.TodoColors
import com.example.todolistssy.presentation.theme.TodoIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAppBar(
    currentDestination: NavDestination?,
    navController: NavHostController
) {

    val routeHistory = stringResource(R.string.route_history)
    val titleHistory = stringResource(R.string.title_history)
    val titleHome = stringResource(R.string.app_title)
    
    TopAppBar(
        title = { 
            Text(
                text = if (currentDestination?.route == routeHistory) {
                    titleHistory
                } else {
                    titleHome
                },
                color = TodoColors.White
            )
        },
        navigationIcon = {
            if (currentDestination?.route == routeHistory) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = TodoColors.White
                    )
                }
            }
        },
        actions = {
            if (currentDestination?.route != routeHistory) {
                IconButton(onClick = {
                    navController.navigate(routeHistory) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Icon(
                        imageVector = TodoIcons.History,
                        contentDescription = titleHistory,
                        tint = TodoColors.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TodoColors.Black
        )
    )
} 