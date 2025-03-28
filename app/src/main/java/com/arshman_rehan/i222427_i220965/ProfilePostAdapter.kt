package com.arshman_rehan.i222427_i220965

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ProfilePostAdapter : RecyclerView.Adapter<ProfilePostAdapter.PostViewHolder>() {
    private val postList = mutableListOf<Post>()

    // Submit a new list of posts and notify the adapter of the change
    fun submitPosts(newPosts: List<Post>) {
        postList.clear()
        postList.addAll(newPosts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_post_image, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.bind(post)
    }

    override fun onViewRecycled(holder: PostViewHolder) {
        super.onViewRecycled(holder)
        holder.clear() // Clear the ImageView to prevent memory leaks
    }

    override fun getItemCount(): Int = postList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postImage: ImageView = itemView.findViewById(R.id.postImage)
        private var currentBitmap: Bitmap? = null // Track the current Bitmap for recycling

        fun bind(post: Post) {
            // Clear the previous Bitmap if it exists
            currentBitmap?.recycle()
            currentBitmap = null
            postImage.setImageBitmap(null)

            // Get the first image from imageUrls (if available)
            val firstImageBase64 = post.imageUrls?.firstOrNull()
            if (firstImageBase64 != null) {
                try {
                    // Decode the Base64 string into a Bitmap
                    val decodedImage = Base64.decode(firstImageBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
                    // Resize the Bitmap to reduce memory usage
                    val resizedBitmap = resizeBitmap(bitmap, 300, 300)
                    currentBitmap = resizedBitmap
                    postImage.setImageBitmap(resizedBitmap)
                } catch (e: Exception) {
                    // Handle invalid Base64 string or other errors
                    postImage.setImageResource(android.R.drawable.ic_menu_gallery) // Fallback image
                }
            } else {
                // No image available
                postImage.setImageResource(android.R.drawable.ic_menu_gallery) // Fallback image
            }
        }

        // Clear the ImageView and recycle the Bitmap
        fun clear() {
            currentBitmap?.recycle()
            currentBitmap = null
            postImage.setImageBitmap(null)
        }

        // Utility function to resize a Bitmap
        private fun resizeBitmap(source: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
            val width = source.width
            val height = source.height
            val aspectRatio = width.toFloat() / height.toFloat()

            var newWidth = maxWidth
            var newHeight = maxHeight
            if (width > height) {
                newHeight = (maxWidth / aspectRatio).toInt()
            } else {
                newWidth = (maxHeight * aspectRatio).toInt()
            }

            return Bitmap.createScaledBitmap(source, newWidth, newHeight, true)
        }
    }
}