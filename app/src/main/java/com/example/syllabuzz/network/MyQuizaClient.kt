package com.example.syllabuzz.network

import com.example.syllabuzz.AppConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyQuizaClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val baseOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val apiService: MyQuizaApiService = buildApiService(baseOkHttpClient)

    val supabaseAuthService: SupabaseAuthService = Retrofit.Builder()
        .baseUrl(AppConfig.SUPABASE_URL + "/")
        .client(baseOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SupabaseAuthService::class.java)

    fun buildAuthenticatedApiService(token: String): MyQuizaApiService {
        val client = baseOkHttpClient.newBuilder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()
        return buildApiService(client)
    }

    private fun buildApiService(client: OkHttpClient): MyQuizaApiService =
        Retrofit.Builder()
            .baseUrl(AppConfig.MYQUIZA_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyQuizaApiService::class.java)
}
