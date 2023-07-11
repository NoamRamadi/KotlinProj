package com.example.socialking.viewmodels

import ChatDataState
import MessageClass
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel(){

    val response: MutableState<ChatDataState> = mutableStateOf(ChatDataState.Empty)



    init {
        fetchDataFromFireBase()
    }

    private fun fetchDataFromFireBase() {
        val tempList = mutableListOf<MessageClass>()
        response.value = ChatDataState.Loading
        FirebaseDatabase.getInstance().getReference("chat").
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (DataSnap in snapshot.children){
                    val msgItem = DataSnap.getValue(MessageClass::class.java)
                    if(msgItem != null) {
                        tempList.add(msgItem)
                    }
                }
                response.value=ChatDataState.Success(tempList)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


}