package com.example.socialking.viewmodels

import DataState
import PostClass
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel : ViewModel(){

    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

    init {
        fetchDataFromFireBase()
    }

    private fun fetchDataFromFireBase() {
        val tempList = mutableListOf<PostClass>()
        response.value = DataState.Loading
        FirebaseDatabase.getInstance().getReference("posts").
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (DataSnap in snapshot.children){
                    val postItem = DataSnap.getValue(PostClass::class.java)
                    if(postItem != null)
                        tempList.add(postItem)
                }
                response.value=DataState.Success(tempList)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}