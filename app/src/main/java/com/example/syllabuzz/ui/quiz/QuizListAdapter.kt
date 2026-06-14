package com.example.syllabuzz.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R
import com.example.syllabuzz.network.model.QuizSummary

class QuizListAdapter(
    private val quizzes: List<QuizSummary>,
    private val onClick: (QuizSummary) -> Unit
) : RecyclerView.Adapter<QuizListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.titleText)
        val subtitleText: TextView = view.findViewById(R.id.subtitleText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.titleText.text = quiz.name
        holder.subtitleText.text = "${quiz.questionCount} questions"
        holder.subtitleText.visibility = View.VISIBLE
        holder.itemView.setOnClickListener { onClick(quiz) }
    }

    override fun getItemCount(): Int = quizzes.size
}
