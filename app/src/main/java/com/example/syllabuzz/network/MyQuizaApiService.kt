package com.example.syllabuzz.network

import com.example.syllabuzz.network.model.*
import retrofit2.http.*

interface MyQuizaApiService {

    @GET("/api/v1/subjects")
    suspend fun getSubjects(): List<Subject>

    @GET("/api/v1/subjects/{id}/chapters")
    suspend fun getChapters(@Path("id") subjectId: String): List<Chapter>

    @GET("/api/v1/chapters/{id}/topics")
    suspend fun getTopics(@Path("id") chapterId: String): List<Topic>

    @GET("/api/v1/topics/{id}/quizzes")
    suspend fun getQuizzes(@Path("id") topicId: String): List<QuizSummary>

    @GET("/api/v1/quizzes/{id}")
    suspend fun getQuizDetail(@Path("id") quizId: String): QuizDetail

    @POST("/api/v1/quizzes/{id}/attempts")
    suspend fun submitAttempt(
        @Path("id") quizId: String,
        @Body body: AttemptRequest
    ): AttemptResponse

    @GET("/api/v1/me/progress")
    suspend fun getMyProgress(): List<ProgressEntry>

    @GET("/api/v1/leaderboard")
    suspend fun getLeaderboard(
        @Query("period") period: String = "weekly",
        @Query("limit") limit: Int = 50
    ): List<LeaderboardEntry>
}
