package com.example.syllabuzz.ui.progress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R

class ProgressAdapter(private val progressList: List<String>) :
    RecyclerView.Adapter<ProgressAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressTextView: TextView = view.findViewById(R.id.progressTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.progressTextView.text = progressList[position]
    }

    override fun getItemCount(): Int = progressList.size
}
