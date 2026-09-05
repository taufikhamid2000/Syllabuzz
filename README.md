# Syllabuzz

A native Android learning app for browsing syllabus content and taking quizzes, with progress tracking and a leaderboard.

**Live demo:** Not applicable — this is a native Android mobile app, not a web-deployable project.

## Overview
Syllabuzz lets students work through subjects broken into chapters and topics, read lesson content, and take quizzes to test what they've learned. It tracks per-user progress and ranks users on a leaderboard, with Supabase-backed authentication. The core app is a native Kotlin Android app; the repo also carries a partial React Native setup (navigation/gesture-handler dependencies) that isn't the primary implementation.

## Tech Stack
- Kotlin, Android (compileSdk 34, minSdk 21), View Binding
- AndroidX (AppCompat, ConstraintLayout, Navigation Component, Lifecycle/ViewModel + LiveData)
- Retrofit + Gson for networking, OkHttp logging interceptor
- Kotlin Coroutines
- Supabase (`SupabaseAuthService`) for authentication
- JUnit + Espresso for testing
- Partial/legacy React Native layer at the repo root (`@react-navigation/*`, `react-native-gesture-handler`, `react-native-reanimated`, etc.)

## Features
- Subjects → Chapters → Topics → Lessons browsing hierarchy (`SubjectsFragment`, `ChaptersFragment`, `TopicsFragment`, `LessonContentFragment`)
- Quizzes with a quiz list and quiz-taking flow (`QuizListFragment`, `QuizFragment`)
- Leaderboard of user rankings (`LeaderboardFragment`, `LeaderboardAdapter`)
- Per-user progress tracking (`ProgressFragment`, `ProgressViewModel`, `ProgressManager`)
- Login and account handling via `LoginFragment` and `SupabaseAuthService` / `AuthManager`
- App settings screen (`SettingsFragment`)
- API access to lesson/quiz data via `MyQuizaApiService` / `MyQuizaRepository` (Retrofit-based)

## Getting Started
Android app (primary):
```bash
git clone https://github.com/taufikhamid2000/Syllabuzz.git
cd Syllabuzz
./gradlew assembleDebug
# or open the project in Android Studio and run on an emulator/device
```

| Config | Purpose |
|---|---|
| Supabase URL / anon key (in `AppConfig.kt` or local config) | Used by `SupabaseAuthService` for authentication |

## Deployment
Not deployed to an app store; run locally via Android Studio or `./gradlew` on a device/emulator.

---
Built by [Muhammad Taufik](https://taufik.vercel.app)
