package com.example.todos.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todos.data.model.User
import com.example.todos.domain.UserState
import com.example.todos.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: UserViewModel = viewModel()
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    // Track selected user
    var selectedUser by remember { mutableStateOf<User?>(null) }

    // Handle Android back button
    BackHandler(enabled = selectedUser != null) {
        selectedUser = null
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (selectedUser == null)
                            "JSONPlaceholder Users"
                        else
                            "${selectedUser!!.name}'s Todos"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    if (selectedUser != null) {
                        IconButton(onClick = { selectedUser = null }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            selectedUser == null -> {
                // Show user list
                when (val state = userState) {
                    is UserState.Loading -> LoadingScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                    is UserState.Error -> ErrorScreen(
                        message = state.message,
                        onRetry = { viewModel.retry() },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                    is UserState.Success -> UserListScreen(
                        users = state.users,
                        onUserClick = { selectedUser = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }
            selectedUser != null -> {
                // Show todos
                TodoScreen(
                    userId = selectedUser!!.id
                )
            }
        }
    }
}
