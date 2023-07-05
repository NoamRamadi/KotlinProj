package com.example.socialking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialking.databinding.ActivityLoggedInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


lateinit var loggedInBinding: ActivityLoggedInBinding
lateinit var discoveredName: String

class LoggedInActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        loggedInBinding = ActivityLoggedInBinding.inflate(layoutInflater)
        val view = loggedInBinding.root
        setContentView(view)





        val intent = intent
        val emailReceived= intent.getStringExtra("email")
        collectNameByEmail(emailReceived.toString())




        //this following segment is for signing out of existing user
        loggedInBinding.SignOutButtonLoggedInAct.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@LoggedInActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        loggedInBinding.addPostButton.setOnClickListener{

            val intent = Intent(this@LoggedInActivity, AddPostActivity::class.java)
            intent.putExtra("email",emailReceived)
            intent.putExtra("name", discoveredName)
            startActivity(intent)

        }

        loggedInBinding.PostWallButtonLoggedInAct.setOnClickListener{

            val intent = Intent(this@LoggedInActivity,PostWallActivity::class.java)
            startActivity(intent)

        }
        loggedInBinding.chatButton.setOnClickListener{

            val intent = Intent(this@LoggedInActivity,ChatActivity::class.java)
            startActivity(intent)

        }



    }

    // Need to add a fun for reading DB and creating LAZYCOLUMN out of it

    fun collectNameByEmail(email: String){
        val modifiedEmail: String = email.replace(".", "")
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(modifiedEmail).get().addOnSuccessListener {
            if (it.exists()) {
                discoveredName= it.child("name").value.toString()
            } else {
                //Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to read data", Toast.LENGTH_SHORT).show()

        }

    }

}
