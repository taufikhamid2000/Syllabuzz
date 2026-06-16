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

        val count = "${quiz.questionCount} question${if (quiz.questionCount != 1) "s" else ""}"
        if (quiz.verified) {
            holder.subtitleText.text = count
            holder.subtitleText.setTextColor(COLOR_NORMAL)
        } else {
            holder.subtitleText.text = "$count · Pending review — won't count toward progress"
            holder.subtitleText.setTextColor(COLOR_UNVERIFIED)
        }
        holder.subtitleText.visibility = View.VISIBLE
        holder.itemView.setOnClickListener { onClick(quiz) }
    }

    private companion object {
        const val COLOR_NORMAL = 0xFF666666.toInt()
        const val COLOR_UNVERIFIED = 0xFFB26A00.toInt() // amber
    }

    override fun getItemCount(): Int = quizzes.size
}
