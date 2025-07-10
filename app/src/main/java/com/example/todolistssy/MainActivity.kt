package com.example.todolistssy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistssy.presentation.home.HomeScreen
import com.example.todolistssy.presentation.home.HomeViewModel
import com.example.todolistssy.ui.theme.TodolistssyTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.todolistssy.presentation.history.HistoryScreen
import com.example.todolistssy.presentation.history.HistoryViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodolistssyTheme {
                var isHistoryScreen by remember { mutableStateOf(false) }
                val homeViewModel: HomeViewModel = hiltViewModel()
                val historyViewModel: HistoryViewModel = hiltViewModel()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.app_title))
                            },
                            navigationIcon = {
                                if (isHistoryScreen) {
                                    IconButton(onClick = { isHistoryScreen = false }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "뒤로가기"
                                        )
                                    }
                                }
                            },
                            actions = {
                                if (!isHistoryScreen) {
                                    IconButton(
                                        onClick = { isHistoryScreen = true }
                                    ) {
                                        Icon(
                                            imageVector = AppIcons.History,
                                            contentDescription = "History"
                                        )
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    if (isHistoryScreen) {
                        HistoryScreen(
                            viewModel = historyViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        HomeScreen(
                            viewModel = homeViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}