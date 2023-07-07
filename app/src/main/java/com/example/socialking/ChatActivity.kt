package com.example.socialking
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.socialking.databinding.ActivityChatBinding
import com.example.socialking.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import android.os.AsyncTask

lateinit var chatBinding: ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var chatView: TextView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatBinding = ActivityChatBinding.inflate(layoutInflater)
        val view = chatBinding.root
        setContentView(view)

        // Initialize UI components
        //chatView = findViewById(R.id.chatView)
        //messageInput = findViewById(R.id.messageInput)
        //sendButton = findViewById(R.id.sendButton)

        // Set up socket connection in a background thread
        ConnectTask().execute()

        // Send button click listener
        chatBinding.sendButton.setOnClickListener {
            val message = chatBinding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                SendMessageTask().execute(message)
                chatBinding.messageInput.text.clear()
            }
        }
        chatBinding.backButton.setOnClickListener {
            val intent = Intent(this@ChatActivity,LoggedInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayMessage(message: String) {
        chatBinding.messageView.append("$message\n")
        //chatView.append("$message\n")
    }


    override fun onDestroy() {
        super.onDestroy()
        // Close the socket connection when the activity is destroyed
        try {
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class ConnectTask : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void?): Boolean {
            try {
                // Replace HOST and PORT with your server details
                socket = Socket("192.168.136.137", 5000)
                reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                writer = PrintWriter(socket.getOutputStream(), true)

                // Start a separate thread to receive messages
                Thread {
                    while (true) {
                        val message = reader.readLine()
                        runOnUiThread {
                            displayMessage(message)
                        }
                    }
                }.start()

                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
    }

    private inner class SendMessageTask : AsyncTask<String, Void, Unit>() {

        override fun doInBackground(vararg params: String) {
            val message = params[0]
            writer.println(message)
        }
    }
}
