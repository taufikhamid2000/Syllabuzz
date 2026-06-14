package com.example.syllabuzz.repository

import com.example.syllabuzz.auth.AuthManager
import com.example.syllabuzz.network.MyQuizaClient
import com.example.syllabuzz.network.model.*

class MyQuizaRepository(private val authManager: AuthManager) {

    private val api = MyQuizaClient.apiService

    suspend fun getSubjects(): Result<List<Subject>> = runCatching { api.getSubjects() }

    suspend fun getChapters(subjectId: String): Result<List<Chapter>> =
        runCatching { api.getChapters(subjectId) }

    suspend fun getTopics(chapterId: String): Result<List<Topic>> =
        runCatching { api.getTopics(chapterId) }

    suspend fun getQuizzes(topicId: String): Result<List<QuizSummary>> =
        runCatching { api.getQuizzes(topicId) }

    suspend fun getQuizDetail(quizId: String): Result<QuizDetail> =
        runCatching { api.getQuizDetail(quizId) }

    suspend fun submitAttempt(quizId: String, request: AttemptRequest): Result<AttemptResponse> {
        val token = authManager.getValidToken()
            ?: return Result.failure(Exception("not_authenticated"))
        return runCatching {
            // Retrofit needs the token on the request — we rebuild with auth header
            MyQuizaClient.buildAuthenticatedApiService(token).submitAttempt(quizId, request)
        }
    }

    suspend fun getMyProgress(): Result<List<ProgressEntry>> {
        val token = authManager.getValidToken()
            ?: return Result.failure(Exception("not_authenticated"))
        return runCatching {
            MyQuizaClient.buildAuthenticatedApiService(token).getMyProgress()
        }
    }

    suspend fun getLeaderboard(period: String = "weekly"): Result<List<LeaderboardEntry>> =
        runCatching { api.getLeaderboard(period = period) }
}
