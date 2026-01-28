package com.example.todos.data.repository

import com.example.todos.data.model.Todo
import com.example.todos.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository to fetch todos from API
 */
class TodoRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Get todos for a specific user
     * Explicitly returns Result<List<Todo>> to fix type inference issues
     */
    suspend fun getTodos(userId: Int): Result<List<Todo>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTodos(userId)
            val todos = response.body()
            if (!todos.isNullOrEmpty()) {
                Result.success(todos)
            } else {
                Result.failure(Exception("No todos found for this user"))
            }
        } catch (e: java.io.IOException) {
            // This usually means no internet
            Result.failure(Exception("No internet connection. Please check your network settings."))
        } catch (e: Exception) {
            // Other errors (HTTP errors, parsing, etc.)
            Result.failure(Exception("Failed to load todos"))
        }
    }


}
