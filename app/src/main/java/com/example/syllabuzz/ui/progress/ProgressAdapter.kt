package com.example.syllabuzz.ui.progress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R
import com.example.syllabuzz.network.model.ProgressEntry
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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

        val statusLabel = when (entry.status) {
            "completed" -> "Completed"
            "in_progress" -> "In Progress"
            else -> "Attempted"
        }
        val scoreText = entry.score?.let { " · $it%" } ?: ""
        val attemptsText = entry.attempts?.let {
            " · $it attempt${if (it != 1) "s" else ""}"
        } ?: ""

        holder.topicText.text = "$statusLabel$scoreText$attemptsText"

        val dateText = entry.lastAttemptedAt?.let { formatDate(it) } ?: ""
        holder.progressTextView.text = if (dateText.isNotEmpty()) "Last attempt: $dateText" else ""
        holder.progressTextView.visibility =
            if (dateText.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = progressList.size

    private fun formatDate(iso: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val display = SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault())
            display.format(parser.parse(iso)!!)
        } catch (e: Exception) {
            iso.take(10)
        }
    }
}
