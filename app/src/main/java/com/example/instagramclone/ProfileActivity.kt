package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = Firebase.auth
        val user = auth.currentUser

        username_textView.text=user?.displayName
        Glide.with(this).load(user?.photoUrl).circleCrop().into(profile_pic)

        sign_out_button.setOnClickListener {
            auth.signOut()
            val intent= Intent(this,SigninActivity::class.java)
            startActivity(intent)

        }
    }

}