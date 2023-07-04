package com.example.socialking

import PostClass
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialking.databinding.ActivitySelectedPostBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

lateinit var selectedPostBinding: ActivitySelectedPostBinding


class SelectedPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        selectedPostBinding = ActivitySelectedPostBinding.inflate(layoutInflater)
        val view = selectedPostBinding.root
        setContentView(view)


        val intent = intent
        val content = intent.getStringExtra("content").toString()
        val byName = intent.getStringExtra("name").toString()
        val byEmail = intent.getStringExtra("email").toString()
        val title = intent.getStringExtra("title").toString()
        val category = intent.getStringExtra("category").toString()
        val postid = intent.getStringExtra("id").toString()


        val db = Firebase.database
        val dbRef = db.getReference("posts")

        selectedPostBinding.editPostTitle.setText(title)
        selectedPostBinding.editPostCategory.setText(category)
        selectedPostBinding.editPostContent.setText(content)

        selectedPostBinding.editPostButton.setOnClickListener {
            //selectedPostBinding.editPostButton.isClickable = false

            val newTitle = selectedPostBinding.editPostTitle.getText().toString()
            val newCategory = selectedPostBinding.editPostCategory.getText().toString()
            val newContent = selectedPostBinding.editPostContent.getText().toString()

            val editedPost = PostClass(byEmail, byName, newTitle, newCategory, newContent, true, postid)
            dbRef.child(postid.toString()).setValue(editedPost).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext,"Your post has been Updated",Toast.LENGTH_SHORT).show()
                    finish()
                    selectedPostBinding.editPostButton.isClickable = true
                } else {
                    Toast.makeText(
                        applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        selectedPostBinding.deletePostButton.setOnClickListener {
            val deletedPost = PostClass(byEmail, byName, title, category, content, false, postid)
            dbRef.child(postid.toString()).setValue(deletedPost).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext,"Your post has been Deleted",Toast.LENGTH_SHORT).show()
                    finish()
                    selectedPostBinding.editPostButton.isClickable = true
                } else {
                    Toast.makeText(
                        applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

    }
}
