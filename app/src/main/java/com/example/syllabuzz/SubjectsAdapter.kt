package com.example.syllabuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.network.model.Subject

class SubjectsAdapter(
    private val subjects: List<Subject>,
    private val onClick: (Subject) -> Unit
) : RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.subjectTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subject, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subject = subjects[position]
        holder.textView.text = subject.name
        holder.itemView.setOnClickListener { onClick(subject) }
    }

    override fun getItemCount(): Int = subjects.size
}
