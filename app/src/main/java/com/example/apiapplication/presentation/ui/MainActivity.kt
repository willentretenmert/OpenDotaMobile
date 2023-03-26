package com.example.apiapplication.presentation.ui

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.apiapplication.R
import com.example.apiapplication.databinding.ActivityMainBinding
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.PlayerStats
import com.google.gson.Gson
import kotlinx.serialization.*
import java.net.URL


class MainActivity : AppCompatActivity() {

    public lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}