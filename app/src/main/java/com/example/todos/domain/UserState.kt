package com.example.todos.domain

import com.example.todos.data.model.Todo
import com.example.todos.data.model.User

/**
 * Sealed class representing different states of the user list screen
 * Sealed classes are perfect for representing a finite set of states
 * Each state can carry different data
 */
sealed class UserState {

    /**
     * Initial/Loading state - shown when data is being fetched
     */
    data object Loading : UserState()

    /**
     * Success state - shown when data is successfully loaded
     * @param users List of User objects to display
     */
    data class Success(val users: List<User>) : UserState()

    /**
     * Error state - shown when an error occurs
     * @param message Error message to display to the user
     */
    data class Error(val message: String) : UserState()
}

sealed class ScreenState {

    data object UserList : ScreenState()

    data class TodoList(
        val userId: Int,
        val userName: String
    ) : ScreenState()
}

sealed class TodoState {
    data object Loading : TodoState()
    data class Success(val todos: List<Todo>) : TodoState()
    data class Error(val message: String) : TodoState()
}
