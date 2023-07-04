package com.example.socialking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.socialking.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        loginBinding.buttonHomeLogin.setOnClickListener{
            val userEmail=loginBinding.editTextEmail.text.toString();
            val userPassword=loginBinding.editTextPassword.text.toString();

            loginUser(userEmail,userPassword)

        }

        loginBinding.buttonHomeRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)

            startActivity(intent)

        }


    }
    fun loginUser(userEmail: String, userPassword: String){

        // this function is from the firebase existing functions
        auth.signInWithEmailAndPassword(userEmail,userPassword) .addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Toast.makeText(applicationContext,"Welcome to Social King",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity,LoggedInActivity::class.java)
                intent.putExtra("email",userEmail)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()

            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//
//        val user = auth.currentUser
//        if(user!=null){
//            Toast.makeText(applicationContext,"Welcome to Social King",Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@LoginActivity,LoggedInActivity::class.java)
//            startActivity(intent)
//            finish()
//
//        }
//    }

}