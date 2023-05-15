package com.example.apiapplication.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.apiapplication.R
import com.example.apiapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    object User {
        var auth = FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navigation : BottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.visibility = View.VISIBLE
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navigation.setupWithNavController(navController)
    }
}