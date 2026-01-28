package com.example.todos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todos.data.model.Todo
import com.example.todos.domain.TodoState
import com.example.todos.ui.viewmodel.TodoViewModel

@Composable
fun TodoScreen(
    userId: Int,
    viewModel: TodoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState(initial = TodoState.Loading)

    // Load todos for this user
    LaunchedEffect(userId) {
        viewModel.loadTodos(userId)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is TodoState.Loading -> CircularProgressIndicator()
            is TodoState.Error -> {
                // Use your reusable ErrorScreen composable
                val errorState = state as TodoState.Error
                ErrorScreen(
                    message = errorState.message,
                    onRetry = { viewModel.loadTodos(userId) }
                )
            }
            is TodoState.Success -> {
                val todos = (state as TodoState.Success).todos
                TodoList(todos)
            }
        }
    }
}

@Composable
fun TodoList(todos: List<Todo>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(todos, key = { it.id }) { todo ->
            TodoItem(todo)
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (todo.completed)
                    Icons.Default.CheckBox
                else
                    Icons.Default.CheckBoxOutlineBlank,
                contentDescription = if (todo.completed) "Completed" else "Not completed"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(todo.title)
        }
    }
}


