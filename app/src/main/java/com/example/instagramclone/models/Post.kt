package com.example.instagramclone.models

data class Post (
    val text: String = "",
    val createdBy: User = User(),
    val createdAt: Long = 0L,
    val postImageUrl:String="",
    val likedBy: ArrayList<String> = ArrayList())