package com.example.syllabuzz.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R

class SettingsAdapter(private val settingsOptions: List<String>) :
    RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val optionTextView: TextView = view.findViewById(R.id.settingsOptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_settings_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.optionTextView.text = settingsOptions[position]
        holder.optionTextView.setOnClickListener {
            // Handle item click, if needed
        }
    }

    override fun getItemCount(): Int = settingsOptions.size
}
