package com.example.socialking
import Server
import com.example.socialking.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ChatActivity : AppCompatActivity() {

    private lateinit var messageView: TextView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    private lateinit var server: Server

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageView = findViewById(R.id.messageView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        server = Server(3000)

        Thread {
            server.start { message ->
                runOnUiThread {
                    messageView.append("Server: $message\n")
                }
            }
        }.start()

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                server.sendMessage(message)
                runOnUiThread {
                    messageView.append("You: $message\n")
                }
                messageInput.text.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
    }
}


