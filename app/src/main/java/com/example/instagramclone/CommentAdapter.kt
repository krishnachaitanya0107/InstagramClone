package com.example.instagramclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.models.Comment
import com.example.instagramclone.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CommentAdapter(options: FirestoreRecyclerOptions<Comment>) : FirestoreRecyclerAdapter<Comment, CommentAdapter.CommentViewHolder>(
    options
) {

    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val commentUserName:TextView=itemView.findViewById(R.id.commentedByUserName)
        val commentUserImage:ImageView=itemView.findViewById(R.id.commentedByUserImage)
        val createdTime:TextView=itemView.findViewById(R.id.createdTime)
        val commentText:TextView=itemView.findViewById(R.id.commentTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val viewHolder =  CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))

        return viewHolder
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, model: Comment) {
        holder.commentText.text = model.comment
        holder.commentUserName.text = model.displayName
        Glide.with(holder.commentUserImage.context).load(model.imageUrl).circleCrop().into(holder.commentUserImage)
        holder.createdTime.text = Utils.getTimeAgo(model.createdAt)
    }
}

