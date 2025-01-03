package com.example.syllabuzz.data

object LessonData {

    private val lessonsMap = mapOf(
        "Bahasa Melayu" to listOf(
            "Lesson 1: Tatabahasa" to "Details about Tatabahasa",
            "Lesson 2: Komsas" to "Details about Komsas",
            "Lesson 3: Oral Presentation Skills" to "Master public speaking in Bahasa Melayu."
        ),
        "English" to listOf(
            "Lesson 1: Grammar" to "Details about Grammar",
            "Lesson 2: Literature" to "Details about Literature"
        ),
        "Mathematics" to listOf(
            "Lesson 1: Algebra" to "Details about Algebra",
            "Lesson 2: Geometry" to "Details about Geometry",
            "Lesson 3: Introduction to Statistics" to "Explore basic statistics."
        ),
        "Science" to listOf(
            "Lesson 1: Biology Basics" to "Details about Biology",
            "Lesson 2: Chemistry Basics" to "Details about Chemistry"
        ),
        "History" to listOf(
            "Lesson 1: Ancient Civilizations" to "Details about Civilizations",
            "Lesson 2: Modern History" to "Details about Modern History"
        )
    )

    // Returns all lessons grouped by subject
    fun getAllLessons(): Map<String, List<Pair<String, String>>> {
        return lessonsMap
    }

    // Returns lessons for a specific subject
    fun getLessons(subject: String): List<Pair<String, String>> {
        return lessonsMap[subject] ?: emptyList()
    }
}
