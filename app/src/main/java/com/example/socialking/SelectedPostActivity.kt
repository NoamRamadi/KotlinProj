package com.example.socialking

import PostClass
import android.content.Intent
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
        val user = intent.getStringExtra("user").toString()

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

            if (!user.equals(byEmail)&& !user.equals("admin@admin.com")) {
                Toast.makeText(applicationContext, "Not allowed", Toast.LENGTH_SHORT).show()
            } else {
                val editedPost =
                    PostClass(byEmail, byName, newTitle, newCategory, newContent, true, postid)
                dbRef.child(postid.toString()).setValue(editedPost).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Your post has been Updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        selectedPostBinding.editPostButton.isClickable = true
                        var intent = Intent(this@SelectedPostActivity, PostWallActivity::class.java)
                        intent.putExtra("email", user)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
        selectedPostBinding.backToPostWall.setOnClickListener {
            var intent = Intent(this@SelectedPostActivity, PostWallActivity::class.java)
            intent.putExtra("email", user)
            startActivity(intent)
            finish()
        }
        selectedPostBinding.deletePostButton.setOnClickListener {

            if (!user.equals(byEmail) && !user.equals("admin@admin.com")) {
                Toast.makeText(applicationContext, "Not allowed", Toast.LENGTH_SHORT).show()
            } else {
                val deletedPost =
                    PostClass(byEmail, byName, title, category, content, false, postid)
                dbRef.child(postid.toString()).setValue(deletedPost).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Your post has been Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        selectedPostBinding.editPostButton.isClickable = true
                        var intent = Intent(this@SelectedPostActivity, PostWallActivity::class.java)
                        intent.putExtra("email", user)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Your post has been Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }

        }
    }
}
