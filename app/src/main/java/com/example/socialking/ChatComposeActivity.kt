package com.example.socialking

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ChatComposeActivity : ComponentActivity() {

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private lateinit var executor: Executor
    private val chatMessages = mutableStateListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatScreen()
        }

        // Set up socket connection in a background thread
        executor = Executors.newSingleThreadExecutor()
        connect()
    }

    private fun displayMessage(message: String) {
        chatMessages.add(message)
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

    private fun connect() {
        executor.execute {
            try {
                // Replace HOST and PORT with your server details
                socket = Socket("192.168.244.137", 5000)
                reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                writer = PrintWriter(socket.getOutputStream(), true)

                // Start a separate thread to receive messages
                Thread {
                    while (true) {
                        val message = reader.readLine()
                        runOnUiThread {
                            chatMessages.add(message)
                        }
                    }
                }.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun sendMessage(vararg params: String) {
        val intent = intent
        val username = intent.getStringExtra("name").toString()
        val message = "\n$username:\n${params[0]}"
        executor.execute {
            writer.println(message)
        }
    }

    @Composable
    fun ChatScreen() {
        val messageInput = remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chatMessages) { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = messageInput.value,
                    onValueChange = { messageInput.value = it },
                    label = { Text("Enter your message") },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        val message = messageInput.value.text.trim()
                        if (message.isNotEmpty()) {
                            sendMessage(message)
                            messageInput.value = TextFieldValue()
                        }
                    }
                ) {
                    Text("Send")
                }
            }

            Button(
                onClick = {
                    val intent = Intent(this@ChatComposeActivity, LoggedInActivity::class.java)
                    startActivity(intent)
                }
            ) {
                Text("Back")
            }
        }

        // Fetch initial chat messages when the screen is created
        LaunchedEffect(Unit) {
            connect()
        }
    }
    private suspend fun fetchMessagesFromServer(): List<String> {
        val url = URL("192.168.122.1") // Replace with your server URL
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val messages = mutableListOf<String>()
            var line: String? = reader.readLine()
            while (line != null) {
                messages.add(line)
                line = reader.readLine()
            }
            reader.close()
            return messages
        } else {
            throw IOException("Failed to fetch messages. Response code: $responseCode")
        }
    }
}




