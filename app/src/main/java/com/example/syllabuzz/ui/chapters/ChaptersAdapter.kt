package com.example.syllabuzz.ui.chapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R
import com.example.syllabuzz.network.model.Chapter

class ChaptersAdapter(
    private val chapters: List<Chapter>,
    private val onClick: (Chapter) -> Unit
) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {

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
        val chapter = chapters[position]
        holder.titleText.text = chapter.name
        holder.subtitleText.text = "Form ${chapter.form}"
        holder.subtitleText.visibility = View.VISIBLE
        holder.itemView.setOnClickListener { onClick(chapter) }
    }

    override fun getItemCount(): Int = chapters.size
}
