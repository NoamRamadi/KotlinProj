package com.example.socialking
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.socialking.databinding.ActivityChatBinding;
import com.example.socialking.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

lateinit var chatBinding: ActivityChatBinding;

class ChatActivity : AppCompatActivity() {

    private lateinit var socket: Socket;
    private lateinit var reader: BufferedReader;
    private lateinit var writer: PrintWriter;
    private lateinit var executor: Executor;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        chatBinding = ActivityChatBinding.inflate(layoutInflater);
        val view = chatBinding.root;
        setContentView(view);

        // Set up socket connection in a background thread
        executor = Executors.newSingleThreadExecutor();
        connect();

        // Send button click listener
        chatBinding.sendButton.setOnClickListener {
            val message = chatBinding.messageInput.text.toString().trim();
            if (message.isNotEmpty()) {
                sendMessage(message);
                chatBinding.messageInput.text.clear();
            }
        };

        chatBinding.backButton.setOnClickListener {
            val intent = Intent(this@ChatActivity, LoggedInActivity::class.java);
            startActivity(intent);
        };
    }

    private fun displayMessage(message: String) {
        chatBinding.messageView.append("$message\n");
    }

    override fun onDestroy() {
        super.onDestroy();
        // Close the socket connection when the activity is destroyed
        try {
            socket.close();
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    private fun connect() {
        executor.execute {
            try {
                // Replace HOST and PORT with your server details
                socket = Socket("192.168.12.137", 5000);
                reader = BufferedReader(InputStreamReader(socket.getInputStream()));
                writer = PrintWriter(socket.getOutputStream(), true);

                // Start a separate thread to receive messages
                Thread {
                    while (true) {
                        val message = reader.readLine();
                        runOnUiThread {
                            displayMessage(message);
                        }
                    }
                }.start();
            } catch (e: Exception) {
                e.printStackTrace();
            }
        };
    }

    private fun sendMessage(vararg params: String) {
        val intent = intent;
        val Username = intent.getStringExtra("nameChat").toString();
        val message = "\n" + Username + ":\n" + params[0];
        executor.execute {
            writer.println(message);
        };
    }
}