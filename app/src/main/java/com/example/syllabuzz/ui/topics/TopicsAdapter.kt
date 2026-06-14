package com.example.syllabuzz.ui.topics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R
import com.example.syllabuzz.network.model.Topic

class TopicsAdapter(
    private val topics: List<Topic>,
    private val onClick: (Topic) -> Unit
) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

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
        val topic = topics[position]
        holder.titleText.text = topic.name
        val parts = mutableListOf<String>()
        topic.difficultyLevel?.let { parts.add("Difficulty $it/5") }
        topic.timeEstimateMinutes?.let { parts.add("~${it}min") }
        if (parts.isNotEmpty()) {
            holder.subtitleText.text = parts.joinToString(" · ")
            holder.subtitleText.visibility = View.VISIBLE
        } else {
            holder.subtitleText.visibility = View.GONE
        }
        holder.itemView.setOnClickListener { onClick(topic) }
    }

    override fun getItemCount(): Int = topics.size
}
