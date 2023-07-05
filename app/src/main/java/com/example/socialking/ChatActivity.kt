package com.example.socialking
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ChatActivity : AppCompatActivity() {

    private lateinit var messageView: TextView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageView = findViewById(R.id.messageView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        connectToServer()

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessageToServer(message)
                messageInput.text.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromServer()
    }

    private fun connectToServer() {
        Thread {
            try {
                // Replace SERVER_IP and SERVER_PORT with the appropriate values for your server
                socket = Socket("localhost", 3000)

                reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                writer = PrintWriter(socket.getOutputStream(), true)

                while (true) {
                    val message = reader.readLine()
                    if (message != null) {
                        runOnUiThread {
                            messageView.append("Server: $message\n")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    reader.close()
                    writer.close()
                    socket.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun sendMessageToServer(message: String) {
        writer.println(message)
        runOnUiThread {
            messageView.append("You: $message\n")
        }
    }

    private fun disconnectFromServer() {
        try {
            reader.close()
            writer.close()
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


