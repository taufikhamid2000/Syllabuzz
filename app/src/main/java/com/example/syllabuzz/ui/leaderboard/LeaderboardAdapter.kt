package com.example.syllabuzz.ui.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R
import com.example.syllabuzz.network.model.LeaderboardEntry

class LeaderboardAdapter(
    private val entries: List<LeaderboardEntry>
) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rankText: TextView = view.findViewById(R.id.rankText)
        val displayName: TextView = view.findViewById(R.id.displayName)
        val levelText: TextView = view.findViewById(R.id.levelText)
        val xpText: TextView = view.findViewById(R.id.xpText)
        val weeklyXpText: TextView = view.findViewById(R.id.weeklyXpText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.rankText.text = "#${position + 1}"
        holder.displayName.text = entry.displayName ?: "Anonymous"
        holder.levelText.text = "Level ${entry.level}"
        holder.xpText.text = "${entry.xp} XP"
        holder.weeklyXpText.text = "+${entry.weeklyXp} this week"
    }

    override fun getItemCount(): Int = entries.size
}
