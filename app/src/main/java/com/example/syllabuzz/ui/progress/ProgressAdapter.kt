package com.example.syllabuzz.ui.progress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R
import com.example.syllabuzz.network.model.ProgressEntry

class ProgressAdapter(private val progressList: List<ProgressEntry>) :
    RecyclerView.Adapter<ProgressAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val topicText: TextView = view.findViewById(R.id.subjectTextView)
        val progressTextView: TextView = view.findViewById(R.id.progressTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = progressList[position]
        holder.topicText.text = entry.topicId ?: "Unknown Topic"
        val statusLabel = when (entry.status) {
            "completed" -> "Completed"
            "in_progress" -> "In Progress"
            else -> "Not Started"
        }
        val scoreText = entry.score?.let { " · Score: $it%" } ?: ""
        val attemptsText = entry.attempts?.let { " · $it attempt${if (it != 1) "s" else ""}" } ?: ""
        holder.progressTextView.text = "$statusLabel$scoreText$attemptsText"
    }

    override fun getItemCount(): Int = progressList.size
}
