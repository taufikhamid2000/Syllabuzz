package com.example.syllabuzz.utils

import android.content.Context

class ProgressManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("lesson_progress", Context.MODE_PRIVATE)

    fun isLessonCompleted(lessonName: String): Boolean {
        return sharedPreferences.getBoolean(lessonName, false)
    }

    fun setLessonCompleted(lessonName: String, completed: Boolean) {
        sharedPreferences.edit().putBoolean(lessonName, completed).apply()
    }
}
