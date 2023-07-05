package com.example.socialking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialking.databinding.ActivityRegisterBinding
import com.example.socialking.databinding.ActivitySearchBinding


lateinit var searchActivtyBinding: ActivitySearchBinding


class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchActivtyBinding = ActivitySearchBinding.inflate(layoutInflater)
        val view = searchActivtyBinding.root
        setContentView(view)



        searchActivtyBinding.buttonBackToLoggedInFromSearch.setOnClickListener {
            val intent = Intent(this@SearchActivity,LoggedInActivity::class.java)
            startActivity(intent)
        }

        searchActivtyBinding.searchNameButton.setOnClickListener {
            val nameToSearch = searchActivtyBinding.editTextSearchName.text.toString()
            val intent = Intent(this@SearchActivity,ResultFromSearchActivity::class.java)
            intent.putExtra("nameToSearch",nameToSearch)
            startActivity(intent)
        }
        searchActivtyBinding.searchByContent.setOnClickListener {
            val contentToSearch = searchActivtyBinding.editTextSearchContent.text.toString()
            val intent = Intent(this@SearchActivity,ResultFromSearchActivity::class.java)
            intent.putExtra("contentToSearch",contentToSearch)
            startActivity(intent)

        }

        searchActivtyBinding.searchCategoryButton.setOnClickListener {
            val categoryToSearch = searchActivtyBinding.editTextSearchCategory.text.toString()
            val intent = Intent(this@SearchActivity,ResultFromSearchActivity::class.java)
            intent.putExtra("categoryToSearch",categoryToSearch)
            startActivity(intent)

        }





        }
}