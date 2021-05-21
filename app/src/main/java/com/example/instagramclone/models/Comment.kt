package com.example.instagramclone.models

data class Comment(val uid:String="",
                   val comment:String="",
                   val displayName:String?="",
                   val imageUrl:String="",
                   val createdAt:Long=0L)