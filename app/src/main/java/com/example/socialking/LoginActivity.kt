package com.example.socialking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.socialking.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context

import android.graphics.Color
import android.os.Build

import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService


class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val CHANNEL_ID = "LOGIN_SUCCESS_CHANNEL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        loginBinding.buttonHomeLogin.setOnClickListener {
            val userEmail = loginBinding.editTextEmail.text.toString()
            val userPassword = loginBinding.editTextPassword.text.toString()

            loginUser(userEmail, userPassword)
        }

        loginBinding.buttonHomeRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(userEmail: String, userPassword: String) {
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //showToast("Welcome to Social King")
                    showLoginNotification()
                    val intent = Intent(this@LoginActivity, LoggedInActivity::class.java)
                    intent.putExtra("email", userEmail)
                    startActivity(intent)
                    finish()
                } else {
                    showToast(task.exception?.localizedMessage)
                }
            }
    }

    private fun showToast(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoginNotification() {
        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Login Success"
            val descriptionText = "Login success notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.BLUE
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.your_image)
            .setContentTitle("Social King")
            .setContentText("Login successful!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Create an explicit intent for the logged-in activity
        val resultIntent = Intent(this, LoggedInActivity::class.java)

        // Create the task stack builder
        val stackBuilder = TaskStackBuilder.create(this)
            .addNextIntentWithParentStack(resultIntent)

        // Get the PendingIntent containing the entire back stack
        val resultPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Set the content intent
        builder.setContentIntent(resultPendingIntent)
        builder.setAutoCancel(true)

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            notify(123, builder.build())
        }
    }
}
