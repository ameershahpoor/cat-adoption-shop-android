package com.example.catsadoption_shop.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InsecureApiService {

    // --- IDOR endpoint (main demo) ---
    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): Response<UserProfile>

    // --- Authentication ---
    @POST("auth/login")
    suspend fun login(
        @Body body: AuthRequest
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body body: AuthRequest
    ): Response<AuthResponse>
}
