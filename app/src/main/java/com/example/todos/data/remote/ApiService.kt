package com.example.todos.data.remote

import com.example.todos.data.model.Todo
import com.example.todos.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for JSONPlaceholder endpoints
 */
interface ApiService {

    /**
     * Fetches all users from the /users endpoint
     * @return Response containing a list of User objects
     */
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("todos")
    suspend fun getTodos(@Query("userId") userId: Int): Response<List<Todo>>
}