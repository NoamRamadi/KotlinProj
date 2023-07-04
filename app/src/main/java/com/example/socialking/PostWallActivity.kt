package com.example.socialking

import PostClass
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialking.repository.PersonRepository

import com.example.socialking.ui.theme.SocialKingTheme
import com.example.socialking.viewmodels.MainViewModel

class PostWallActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialKingTheme {
                //val personRepository = PersonRepository()
                //val getAllData = personRepository.getAllData()
                
                
                   Column{
                       TopAppBar (
                           title ={
                               Text(text="User Posts")
                           },
                       )
                       SetData(viewModel)
                       }
                   }
            }
        }
    @Composable
    fun SetData(viewModel: MainViewModel) {
        when (val result = viewModel.response.value){
            is DataState.Loading -> {
               Box(
                   modifier = Modifier.fillMaxSize(),
                   contentAlignment = Alignment.Center
               ){
                   CircularProgressIndicator()
               }

            }
            is DataState.Success -> {
                ShowLazyList(result.data)

            }
            is DataState.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text=result.message,
                        fontSize= MaterialTheme.typography.h5.fontSize,
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text="Error Fetching data",
                        fontSize= MaterialTheme.typography.h5.fontSize,
                    )
                }
            }
        }
        
    }

     @Composable
    fun ShowLazyList(posts: MutableList<PostClass> ) {
        LazyColumn{
            items(posts){ post ->
                CardItem(post)
            }
        }

    }

    @Composable
    fun CardItem(post: PostClass) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = 4.dp
        )
        {
            Column(modifier = Modifier.padding(16.dp)){
                Row(modifier = Modifier.fillMaxWidth() ){
                    Text(text = post.byName.toString(),modifier = Modifier.weight(1f))
                    Text(text=post.category.toString())
                }
                Text(
                    text=post.title.toString(),
                    modifier=Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    style = MaterialTheme.typography.h6
                )
                Text(text = post.content.toString(),
                modifier = Modifier.fillMaxWidth())
            }

    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
@Composable
fun CustomText(text : String){
    Text(
        text = text,

    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SocialKingTheme {
        val personRepository = PersonRepository()
        val getAllData = personRepository.getAllData()

        LazyColumn(

            verticalArrangement = Arrangement.SpaceBetween
        ){
            itemsIndexed(items = getAllData){index , person ->

                CustomItem(person = person)
            }
        }
    }
}}