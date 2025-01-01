package com.example.syllabuzz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class SubjectsAdapter(private val subjects: List<String>) :
    RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.subjectTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subject, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = subjects[position]

        holder.textView.setOnClickListener {
            val subjectName = subjects[position]
            val bundle = Bundle()
            bundle.putString("subjectName", subjectName)

            val navController = it.findNavController()
            navController.navigate(R.id.action_subjectsFragment_to_lessonsFragment, bundle)
        }
    }

    override fun getItemCount(): Int = subjects.size
}