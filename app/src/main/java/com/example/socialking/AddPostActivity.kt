package com.example.socialking

import PostClass
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialking.databinding.ActivityAddPostBinding
import com.example.socialking.databinding.ActivityRegisterBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

lateinit var addPostBinding: ActivityAddPostBinding




class AddPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPostBinding = ActivityAddPostBinding.inflate(layoutInflater)
        val view = addPostBinding.root

        setContentView(view)

        


        val intent = intent
        val byEmail= intent.getStringExtra("email").toString()
        val byName= intent.getStringExtra("name").toString()

        val isVisible: Boolean = true

        addPostBinding.publishPostButton.setOnClickListener {
            val title = addPostBinding.editTextTitle.text.toString()
            val category = addPostBinding.editTextCategory.text.toString()
            val content = addPostBinding.editTextContent.text.toString()

            publishPost(byEmail,byName,title,category,content,isVisible)

            }





    }
        fun publishPost(byEmail: String, byName: String,title: String,
                        category: String,content: String,isVisible: Boolean){
            addPostBinding.publishPostButton.isClickable = false
            val postid = Counter.counter.toString()
            val newPost = PostClass(byEmail,byName,title,category,content,isVisible,postid)
            val db = Firebase.database
            val dbRef = db.getReference("posts")
            dbRef.child(postid.toString()).setValue(newPost).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext,"Your post has been published",Toast.LENGTH_SHORT).show()
                    Counter.count()
                    finish()
                    addPostBinding.publishPostButton.isClickable = true
                } else {
                    Toast.makeText(
                        applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                    ).show()


                }

        }
}
}