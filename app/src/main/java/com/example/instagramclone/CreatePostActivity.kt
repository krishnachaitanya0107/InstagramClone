package com.example.instagramclone

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.daos.PostDao
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_post.*
import java.io.IOException


class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var filePath: Uri
    private lateinit var imageView:ImageView
    val storage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference: StorageReference =storage.reference
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        imageView=findViewById(R.id.postImageView)

        val currentUserId = auth.currentUser!!.uid


        chooseImageButton.setOnClickListener {
            chooseImage()
        }
        postDao = PostDao()
        postButton.setOnClickListener {
            val input=postInput.text.toString()
            if(input.isNotEmpty() && filePath.toString().isNotEmpty()) {
                postButton.isEnabled=false
                chooseImageButton.isEnabled=false
                uploadingProgressBar.visibility= View.VISIBLE
                Toast.makeText(this,
                    "Uploading !!",
                    Toast.LENGTH_SHORT).show()

                val currentTime = System.currentTimeMillis()
                val randomID= currentUserId +currentTime.toString()

                var downloadUrl=""
                val ref=storageReference.child("images").child(randomID)

                val uploadTask=ref.putFile(filePath)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }.addOnCompleteListener {
                    if(it.isSuccessful) {
                        downloadUrl=it.result.toString()
                        //Log.i("download",downloadUrl)

                        postDao.addPost(input, downloadUrl)

                        uploadingProgressBar.visibility=View.GONE
                        chooseImageButton.isEnabled=true
                        postButton.isEnabled=true

                        finish()
                    }
                }
            } else
            {
                Toast.makeText(applicationContext,
                    "Please add some text and an image in the post",
                    Toast.LENGTH_SHORT).show()
            }
        }
        setUpRecyclerView()
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            filePath = data?.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun setUpRecyclerView() {

    }
}