package com.example.socialking

import MessageClass
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ChatComposeActivity : ComponentActivity() {

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private lateinit var executor: Executor
    private val chatMessages = mutableStateListOf<String>()
    private lateinit var chatRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatScreen()
        }

        // Set up socket connection in a background thread


        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        chatRef = database.getReference("chat")

        executor = Executors.newSingleThreadExecutor()
        connect()
        fetchInitialChatMessages()


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

    /*
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
    */
    private fun connect() {
        executor.execute {
            try {
                // Replace HOST and PORT with your server details
                socket = Socket("192.168.24.137", 5000)
                reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                writer = PrintWriter(socket.getOutputStream(), true)

                // Start a separate thread to receive messages
                Thread {
                    while (true) {
                        val message = reader.readLine()
                        runOnUiThread {
                            if (!chatMessages.contains(message)) {
                                chatMessages.add(message)
                            }
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
        val message = params[0]
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        val modifiedDate = formattedDate.replace(":", " ")

        // Create a MessageClass object
        val newMessage = MessageClass(formattedDate, username, message)

        // Store the new message in the Firebase Realtime Database

        chatRef.child(modifiedDate).setValue(newMessage)

        executor.execute {
            val msgToDisplay = username + ": " + message + "    " + formattedDate
            writer.println(msgToDisplay)
        }
    }


    @Composable
    fun ChatScreen() {
        val messageInput = remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chatMessages) { message ->
                    ChatMessage(message)
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
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Send")
                }
            }

            Button(
                onClick = {
                    val intent = Intent(this@ChatComposeActivity, LoggedInActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Back")
            }
        }
        // Fetch initial chat messages when the screen is created
        LaunchedEffect(Unit)
        {
            connect()
        }
    }

    @Composable
    fun ChatMessage(message: String) {
        Text(
            text = message,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp)
                .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
    }

    /*
        private fun fetchInitialChatMessages() {
            chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<MessageClass>()
                    for (childSnapshot in snapshot.children) {
                        val message = childSnapshot.getValue(MessageClass::class.java)
                        message?.let {
                            messages.add(it)
                        }
                    }
                    chatMessages.addAll(messages.map { "${it.name}: ${it.message}" + "    " + "${it.time}" })

                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error here
                }
            })
        }
    */
    private fun fetchInitialChatMessages() {
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<String>()
                for (childSnapshot in snapshot.children) {
                    val message = childSnapshot.getValue(MessageClass::class.java)
                    message?.let {
                        val formattedMessage = "${it.name}: ${it.message}    ${it.time}"
                        if (!chatMessages.contains(formattedMessage)) {
                            messages.add(formattedMessage)
                        }
                    }
                }
                chatMessages.addAll(messages)
                }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error here
            }
        })
    }
}




