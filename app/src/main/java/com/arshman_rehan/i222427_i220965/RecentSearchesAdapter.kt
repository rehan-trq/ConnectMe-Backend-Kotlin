package com.arshman_rehan.i222427_i220965

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecentSearchesAdapter(
    private val recentSearches: MutableList<String>,
    private val onRemoveClick: (String) -> Unit,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<RecentSearchesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.searchUsername)
        val crossIcon: ImageView = itemView.findViewById(R.id.crossIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_searches, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchQuery = recentSearches[position]
        holder.usernameTextView.text = searchQuery
        holder.crossIcon.setOnClickListener {
            onRemoveClick(searchQuery)
        }
        holder.itemView.setOnClickListener {
            onClick(searchQuery)
        }
    }

    override fun getItemCount(): Int = recentSearches.size
}