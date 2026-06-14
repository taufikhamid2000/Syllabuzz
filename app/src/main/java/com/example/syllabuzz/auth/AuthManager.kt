package com.example.syllabuzz.auth

import android.content.Context
import android.content.SharedPreferences
import com.example.syllabuzz.AppConfig
import com.example.syllabuzz.network.MyQuizaClient
import com.example.syllabuzz.network.model.RefreshRequest
import com.example.syllabuzz.network.model.SignInRequest

class AuthManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("syllabuzz_auth", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean = getAccessToken() != null && !isTokenExpired()

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    private fun isTokenExpired(): Boolean {
        val expiry = prefs.getLong(KEY_TOKEN_EXPIRY, 0L)
        return System.currentTimeMillis() > expiry - 60_000L
    }

    fun saveTokens(accessToken: String, refreshToken: String, expiresInSeconds: Int) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .putLong(KEY_TOKEN_EXPIRY, System.currentTimeMillis() + expiresInSeconds * 1000L)
            .apply()
    }

    fun clearTokens() {
        prefs.edit().clear().apply()
    }

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            val response = MyQuizaClient.supabaseAuthService.signIn(
                apiKey = AppConfig.SUPABASE_ANON_KEY,
                body = SignInRequest(email, password)
            )
            saveTokens(response.accessToken, response.refreshToken, response.expiresIn)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getValidToken(): String? {
        if (!isTokenExpired()) return getAccessToken()
        val refresh = getRefreshToken() ?: return null
        return try {
            val response = MyQuizaClient.supabaseAuthService.refreshToken(
                apiKey = AppConfig.SUPABASE_ANON_KEY,
                body = RefreshRequest(refresh)
            )
            saveTokens(response.accessToken, response.refreshToken, response.expiresIn)
            response.accessToken
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_EXPIRY = "token_expiry"

        @Volatile
        private var instance: AuthManager? = null

        fun getInstance(context: Context): AuthManager =
            instance ?: synchronized(this) {
                instance ?: AuthManager(context.applicationContext).also { instance = it }
            }
    }
}
