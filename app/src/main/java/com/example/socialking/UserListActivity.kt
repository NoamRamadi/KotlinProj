package com.example.socialking

import UserClass
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class UserListActivity : AppCompatActivity() {
    private lateinit var userList: MutableList<UserClass>
    private lateinit var databaseRef: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userList = mutableListOf()
        databaseRef = FirebaseDatabase.getInstance().getReference("users")
        firebaseAuth = FirebaseAuth.getInstance()

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (userSnapshot in snapshot.children) {
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    val password = userSnapshot.child("password").getValue(String::class.java)
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val id = userSnapshot.child("id").getValue(Int::class.java)

                    email?.let { password?.let { name?.let { id?.let { id -> userList.add(UserClass(email, password, name, id)) } } } }
                }

                setContent {
                    UserListContent(userList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        databaseRef.addValueEventListener(valueEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseRef.removeEventListener(valueEventListener)
    }

    private fun deleteUser(user: UserClass) {
        val modifiedEmail = user.email.replace(".", "")
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val userRef = databaseRef.child(modifiedEmail)

        // Delete user from Realtime Database
        userRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Check if the user to be deleted is the admin user
                if (user.email == "admin@admin.com") {
                    showToast("Cannot delete admin user")
                } else {
                    // Delete user from Firebase Authentication
                    val authEmail = user.email // Store the user's email
                    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                    firebaseAuth.signOut()
                    firebaseAuth.signInWithEmailAndPassword("d@d.com", user.password)
                        .addOnCompleteListener { signInTask ->
                            if (signInTask.isSuccessful) {
                                firebaseUser?.delete()
                                    ?.addOnCompleteListener { authTask ->
                                        if (authTask.isSuccessful) {
                                            showToast("User deleted successfully")
                                        } else {
                                            showToast("Failed to delete user from authentication")
                                        }
                                    }
                            } else {
                                showToast("Failed to delete user from authentication")
                            }
                        }
                }
            } else {
                showToast("Failed to delete user")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    @Composable
    private fun UserListContent(userList: List<UserClass>) {
        Column(modifier = Modifier.fillMaxSize()) {
            userList.forEach { user ->
                UserItem(user) { deleteUser(user) }
            }
        }
    }

    @Composable
    private fun UserItem(user: UserClass, onDeleteUser: (UserClass) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onDeleteUser(user) },
            backgroundColor = Color.Gray,
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Email: ${user.email}",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Name: ${user.name}",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Password: ${user.password}",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "ID: ${user.id}",
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}
