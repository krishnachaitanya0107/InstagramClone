package com.example.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.daos.CommentDao
import com.example.instagramclone.daos.PostDao
import com.example.instagramclone.models.Comment
import com.example.instagramclone.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_main.*

class CommentActivity : AppCompatActivity() {
    private lateinit var commentDao:CommentDao
    private lateinit var adapter:CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        commentDao=CommentDao()

        val intent=intent
        val postId=intent.getStringExtra("postId")

        chat_send_btn.setOnClickListener {
            val commentText:String= chat_message_view.text.toString()
            if(commentText.isNotEmpty())
            {
                commentDao.addComment(commentText, postId.toString())
                chat_message_view.setText("")
                adapter.notifyDataSetChanged()
                Toast.makeText(this,"Comment Added !",Toast.LENGTH_SHORT).show()
            }
        }
        setUpCommentsRecyclerView(postId.toString())
    }

    fun setUpCommentsRecyclerView(postId:String)
    {
        commentDao = CommentDao()
        val commentsCollections = commentDao.commentsCollections
        val query = commentsCollections
            .document(postId).collection("comments")
            .orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions
            .Builder<Comment>().setQuery(query, Comment::class.java).build()

        adapter = CommentAdapter(recyclerViewOptions)

        messages_list.adapter = adapter
        messages_list.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}