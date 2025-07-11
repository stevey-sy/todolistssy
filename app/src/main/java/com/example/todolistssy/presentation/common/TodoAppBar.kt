package com.example.todolistssy.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.todolistssy.R
import com.example.todolistssy.presentation.theme.TodoIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAppBar(
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    TopAppBar(
        title = { 
            Text(
                if (currentDestination?.route == "history") {
                    "History"
                } else {
                    stringResource(R.string.app_title)
                }
            )
        },
        navigationIcon = {
            if (currentDestination?.route == "history") {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (currentDestination?.route != "history") {
                IconButton(onClick = {
                    navController.navigate("history") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Icon(
                        imageVector = TodoIcons.History,
                        contentDescription = "History"
                    )
                }
            }
        }
    )
} 