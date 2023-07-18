package com.example.socialking
import MessageClass
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialking.Counter.chatcounter
import com.example.socialking.databinding.ActivityChatBinding
import com.example.socialking.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

lateinit var chatBinding: ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatBinding = ActivityChatBinding.inflate(layoutInflater)
        val view = chatBinding.root
        setContentView(view)
        //val intent = intent
        val Username = intent.getStringExtra("name").toString()

        // Set up socket connection in a background thread
        executor = Executors.newSingleThreadExecutor()
        //var flag = intent.getStringExtra("flag").toString()
//        if(flag.equals("1")) {
//            connect()
//        }
        connect()
        // Send button click listener
        chatBinding.sendButton.setOnClickListener {
            val message = chatBinding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                chatBinding.messageInput.text.clear()
                updateChat(message,Username)
            }
        }

        chatBinding.backButton.setOnClickListener {

            val name = intent.getStringExtra("name").toString()
            val emailReceived= intent.getStringExtra("email").toString()
            intent.putExtra("email",emailReceived)
            intent.putExtra("name",name)
           // intent.putExtra("flag","0")

            intent = Intent(this@ChatActivity, LoggedInActivity::class.java)

            startActivity(intent)
            onDestroy()
            finish()
        }
    }

    private fun displayMessage(message: String) {
        chatBinding.messageView.append("$message\n")
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
                socket = Socket("192.168.24.137", 5000)
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendMessage(vararg params: String) {
        val intent = intent
        val Username = intent.getStringExtra("name").toString()
        val message = Username + ":\n" + params[0]+ "\n"
        executor.execute {
            writer.println(message)
        }
    }
    fun updateChat( msg: String,name: String){

        val currentDate=Date()
        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm:ss",Locale.getDefault())
        val formattedDate= dateFormat.format(currentDate)
        //Toast.makeText(applicationContext, formattedDate.toString(), Toast.LENGTH_SHORT).show()
        var modifiedDate = formattedDate.toString().replace(":"," ")
        //modifiedDate = modifiedDate.replace(" ","")
        val db = Firebase.database
        val dbRef = db.getReference("chat")


        val newMessage = MessageClass(modifiedDate, name,msg)




        dbRef.child(modifiedDate).setValue(newMessage).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Toast.makeText(applicationContext,"Your post has been published", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(
                    applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                ).show()


            }

        }
    }



}