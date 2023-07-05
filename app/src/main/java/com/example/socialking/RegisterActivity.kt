package com.example.socialking

import UserClass
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialking.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

lateinit var registerBinding: ActivityRegisterBinding
val auth : FirebaseAuth = FirebaseAuth.getInstance()

class RegisterActivity : AppCompatActivity() {
    //private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = registerBinding.root
        setContentView(view)

        // myRef = FirebaseDatabase.getInstance().reference.child("Users")
        val db = Firebase.database
        val dbRef = db.getReference("users")
        registerBinding.buttonRegisterApply.setOnClickListener {
            val email = registerBinding.editTextRegisterEmail.text.toString()
            val password = registerBinding.editTextRegisterPassword.text.toString()
            val name: String = registerBinding.editTextRegisterName.getText().toString()
            val ID: Int = registerBinding.editTextRegisterID.getText().toString().toInt()
            val newUser = UserClass(email, password, name, ID)
            val modifiedEmail: String = newUser.email.replace(".", "")
            dbRef.child(modifiedEmail).setValue(newUser).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Your account has been created",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                    ).show()


                }
            }
            registerWithFirebase(email, password)

        }
    }


    fun registerWithFirebase(email: String, password: String) {

        registerBinding.buttonRegisterApply.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                registerBinding.buttonRegisterApply.isClickable = true
                val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()


            }
        }
    }
}



