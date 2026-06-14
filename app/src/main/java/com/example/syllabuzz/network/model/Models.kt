package com.example.syllabuzz.network.model

import com.google.gson.annotations.SerializedName

data class Subject(
    val id: String,
    val name: String,
    val slug: String,
    val description: String?,
    val icon: String?,
    val category: String?,
    val orderIndex: Int,
    val isDisabled: Boolean
)

data class Chapter(
    val id: String,
    val subjectId: String?,
    val name: String,
    val form: Int,
    val orderIndex: Int
)

data class Topic(
    val id: String,
    val chapterId: String?,
    val name: String,
    val description: String?,
    val difficultyLevel: Int?,
    val timeEstimateMinutes: Int?,
    val orderIndex: Int
)

data class QuizSummary(
    val id: String,
    val topicId: String,
    val name: String,
    val verified: Boolean,
    val questionCount: Int
)

data class QuizDetail(
    val id: String,
    val topicId: String,
    val name: String,
    val verified: Boolean,
    val questions: List<Question>
)

data class Question(
    val id: String,
    val text: String,
    val type: String,
    val orderIndex: Int,
    val options: List<QuizOption>
)

data class QuizOption(
    val id: String,
    val text: String,
    val orderIndex: Int
)

data class AttemptRequest(
    val answers: List<Answer>,
    val timeTaken: Int? = null
)

data class Answer(
    val questionId: String,
    val selectedAnswerIds: List<String>
)

data class AttemptResponse(
    val attemptId: String,
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val maxScore: Int,
    val xpAwarded: Boolean
)

data class ProgressEntry(
    val topicId: String?,
    val status: String?,
    val score: Int?,
    val attempts: Int?,
    val lastAttemptedAt: String?
)

data class LeaderboardEntry(
    val userId: String,
    val displayName: String?,
    val avatarUrl: String?,
    val xp: Int,
    val level: Int,
    val weeklyXp: Int
)

data class SignInRequest(
    val email: String,
    val password: String
)

data class SignInResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String
)

data class RefreshRequest(
    @SerializedName("refresh_token") val refreshToken: String
)
