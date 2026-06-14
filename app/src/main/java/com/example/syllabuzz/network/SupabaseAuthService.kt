package com.example.syllabuzz.network

import com.example.syllabuzz.network.model.RefreshRequest
import com.example.syllabuzz.network.model.SignInRequest
import com.example.syllabuzz.network.model.SignInResponse
import retrofit2.http.*

interface SupabaseAuthService {

    @POST("/auth/v1/token")
    suspend fun signIn(
        @Query("grant_type") grantType: String = "password",
        @Header("apikey") apiKey: String,
        @Body body: SignInRequest
    ): SignInResponse

    @POST("/auth/v1/token")
    suspend fun refreshToken(
        @Query("grant_type") grantType: String = "refresh_token",
        @Header("apikey") apiKey: String,
        @Body body: RefreshRequest
    ): SignInResponse
}
