package com.example.syllabuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProgressAdapter(private val progressData: List<Pair<String, Int>>) :
    RecyclerView.Adapter<ProgressAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subjectTextView: TextView = view.findViewById(R.id.subjectTextView)
        val progressTextView: TextView = view.findViewById(R.id.progressTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (subject, completedCount) = progressData[position]
        holder.subjectTextView.text = subject
        holder.progressTextView.text = "$completedCount lessons completed"
    }

    override fun getItemCount(): Int = progressData.size
}
