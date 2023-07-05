package com.example.socialking

import PostClass
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialking.repository.PersonRepository

import com.example.socialking.ui.theme.SocialKingTheme
import com.example.socialking.viewmodels.MainViewModel


class PostWallActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentRecived = intent
        val user= intentRecived.getStringExtra("email").toString()
        setContent {
            SocialKingTheme {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        TopAppBar(
                            title = {
                                Text(text = "User Posts")
                            },

                            )
                        SetData(viewModel, user)
                    }
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        )
                        {
                            Button(
                                onClick = {
                                    val intent =
                                        Intent(this@PostWallActivity, LoggedInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }, modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            {
                                Text(text = "Back to Login page")
                            }
                        }

                    }
                }
            }
        }
    }


    @Composable
    fun SetData(viewModel: MainViewModel,user : String) {
        when (val result = viewModel.response.value) {
            is DataState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            }
            is DataState.Success -> {
                ShowLazyList(result.data,user)

            }
            is DataState.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = result.message,
                        fontSize = MaterialTheme.typography.h5.fontSize,
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error Fetching data",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                    )
                }
            }
        }

    }

    @Composable
    fun ShowLazyList(posts: MutableList<PostClass>,user:String) {
        val intent = Intent(this, SelectedPostActivity::class.java)
        val selectedPostContent: String = ""
        val selectedPostTitle: String = ""
        val selectedPostCategory: String = ""
        LazyColumn {
            items(posts) { post ->
                if (post.visible == true) {
                    CardItem(post = post) {
                        intent.putExtra("content", post.content)
                        intent.putExtra("name", post.byName)
                        intent.putExtra("email", post.byEmail)
                        intent.putExtra("title", post.title)
                        intent.putExtra("category", post.category)
                        intent.putExtra("id", post.postid)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                }
            }
        }

    }


    @Composable
    fun CardItem(post: PostClass, onClick: (msg: String) -> Unit) {
        val msg ="none"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { onClick(msg) },
            border = BorderStroke(2.dp, Color.Black),
            elevation = 4.dp
        )
        {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = post.byName.toString(), modifier = Modifier.weight(1f))
                    Text(text = post.category.toString())
                }
                Text(
                    text = post.title.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = post.content.toString(),
                    modifier = Modifier.fillMaxWidth()
                )


            }

        }
    }


}



