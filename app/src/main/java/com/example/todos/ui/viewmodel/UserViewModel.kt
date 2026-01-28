package com.example.todos.ui.viewmodel

import androidx.lifecycle. ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todos.data.model.User
import com.example.todos.data.repository.TodoRepository
import com.example.todos.data.repository.UserRepository
import com.example.todos.domain.ScreenState
import com.example.todos.domain.TodoState
import com.example.todos.domain.UserState
import kotlinx.coroutines.flow. MutableStateFlow
import kotlinx. coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.net.SocketTimeoutException

/**
 * ViewModel for managing user list UI state
 * Uses StateFlow for reactive state management
 * Survives configuration changes (like screen rotation)
 */
class UserViewModel :  ViewModel() {

    // Repository instance
    private val repository = UserRepository()

    // Mutable state flow - private, can only be modified within ViewModel
    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.UserList)
    val screenState = _screenState.asStateFlow()

    // Public immutable state flow - exposed to UI
    // UI can collect this but cannot modify it
    val userState:  StateFlow<UserState> = _userState.asStateFlow()

    // Automatically load users when ViewModel is created
    init {
        loadUsers()
    }

    /**
     * Loads users from the repository
     * Launched in viewModelScope - automatically cancelled when ViewModel is cleared
     */
    fun loadUsers() {
        // Launch coroutine in viewModelScope
        viewModelScope. launch {
            // Set state to Loading
            _userState. value = UserState.Loading

            // Call repository to get users
            val result = repository.getUsers()

            // Handle the result
            result.onSuccess { users ->
                // On success, update state with user list
                _userState.value = UserState.Success(users)
            }.onFailure { exception ->
                // On failure, update state with error message
                // Provide user-friendly error messages based on exception type
                val errorMessage = when (exception) {
                    is UnknownHostException -> {
                        // No internet connection
                        "No internet connection. Please check your network settings."
                    }
                    is SocketTimeoutException -> {
                        // Request timeout
                        "Connection timeout. Please try again."
                    }
                    else -> {
                        // Other errors (HTTP errors, parsing errors, etc.)
                        exception.message ?: "An unknown error occurred"
                    }
                }
                _userState.value = UserState.Error(errorMessage)
            }
        }
    }

    /**
     * Retry function to reload users after an error
     * Can be called from the Error screen
     */
    fun retry() {
        loadUsers()
    }

    fun openTodos(user: User) {
        _screenState.value = ScreenState.TodoList(
            userId = user.id,
            userName = user.name
        )
    }

    fun goBackToUsers() {
        _screenState.value = ScreenState.UserList
    }

}


