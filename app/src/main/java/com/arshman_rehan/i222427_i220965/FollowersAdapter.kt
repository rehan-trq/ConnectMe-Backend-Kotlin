package com.arshman_rehan.i222427_i220965

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class FollowerAdapter(
    private val followers: MutableList<Pair<String, userCredential>>
) : RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic: CircleImageView = itemView.findViewById(R.id.followerProfilePic)
        val usernameTextView: TextView = itemView.findViewById(R.id.followerUsername)
        val messageIcon: ImageView = itemView.findViewById(R.id.messageIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_follower, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val followerPair = followers[position]
        val follower = followerPair.second // Get the userCredential

        holder.usernameTextView.text = follower.username

        // Load profile picture
        if (follower.profileImage.isNotEmpty()) {
            try {
                val decodedImage = Base64.decode(follower.profileImage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
                holder.profilePic.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.profilePic.setImageResource(R.drawable.olivia)
            }
        } else {
            holder.profilePic.setImageResource(R.drawable.olivia)
        }

        // Message icon click listener (can be implemented later)
        holder.messageIcon.setOnClickListener {
            // TODO: Navigate to chat page with followerPair.first (UID)
        }
    }

    override fun getItemCount(): Int = followers.size
}