package com.example.syllabuzz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.utils.ProgressManager

class LessonsAdapter(
    private val context: Context,
    private val lessonNames: List<String>,
    private val lessons: List<Pair<String, String>>
) : RecyclerView.Adapter<LessonsAdapter.ViewHolder>() {

    private val progressManager = ProgressManager(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.lessonTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lessonName = lessonNames[position]
        val isCompleted = progressManager.isLessonCompleted(lessonName)

        holder.textView.text = if (isCompleted) "✔ $lessonName" else lessonName

        holder.textView.setOnClickListener {
            val lessonDetails = lessons[position].second

            val bundle = Bundle().apply {
                putString("lessonName", lessonName)
                putString("lessonDetails", lessonDetails)
            }

            val navController = it.findNavController()
            navController.navigate(R.id.action_lessonsFragment_to_lessonContentFragment, bundle)
        }
    }

    override fun getItemCount(): Int = lessonNames.size
}
