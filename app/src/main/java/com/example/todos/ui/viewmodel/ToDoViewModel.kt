package com.example.todos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todos.data.repository.TodoRepository
import com.example.todos.domain.TodoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {

    private val repository = TodoRepository()

    // Internal mutable state
    private val _state = MutableStateFlow<TodoState>(TodoState.Loading)

    // Public read-only state
    val state: StateFlow<TodoState> = _state.asStateFlow()

    /**
     * Load todos for a specific user
     */
    fun loadTodos(userId: Int) {
        viewModelScope.launch {
            _state.value = TodoState.Loading

            try {
                val result = repository.getTodos(userId)
                result
                    .onSuccess { todos ->
                        _state.value = TodoState.Success(todos)
                    }
                    .onFailure { error ->
                        _state.value = TodoState.Error(error.message ?: "Unknown error")
                    }
            } catch (e: Exception) {
                // This is a safeguard; repository should already wrap exceptions in Result
                _state.value = TodoState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
