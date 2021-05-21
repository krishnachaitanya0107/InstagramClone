package com.example.instagramclone.daos

import com.example.instagramclone.models.Comment
import com.example.instagramclone.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CommentDao {
    private val db = FirebaseFirestore.getInstance()
    val commentsCollections = db.collection("posts")
    private val auth = Firebase.auth

    fun addComment(text:String,postId:String)
    {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!

            val currentTime = System.currentTimeMillis()
            val comment=Comment(currentUserId,text,user.displayName,user.imageUrl,currentTime)

            commentsCollections.document(postId)
                    .collection("comments")
                    .document()
                    .set(comment)
        }
    }
}