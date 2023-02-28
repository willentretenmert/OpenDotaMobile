package com.example.apiapplication

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apiapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.*
import kotlinx.serialization.json.Json


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
//    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.button.setOnClickListener {
            if (binding.editText.text.isNotBlank()) {
                val id = "275690206"
                val policy = ThreadPolicy.Builder()
                    .permitAll()
                    .build()
                StrictMode.setThreadPolicy(policy)
                // 1068042013 - my id
                // 275690206 - eugenesub id
                // your codes here
                val request = Request.Builder()
                    .url("https://api.opendota.com/api/players/$id/heroes")
                    .build()
                val response = client.newCall(request).execute()
                val json = response.body?.string() ?: throw IllegalStateException("No response body")
                val obj = Json.decodeFromString<Hero>(json)
                binding.textView.text = json
//                var str = ""
//                getHeroesCollection().forEach {
//                    str += it
//                }
//                binding.textView.text = str
            }
            else Toast.makeText(this, getString(R.string.tv_err), Toast.LENGTH_SHORT).show()
        }
    }
//    private fun getHeroesCollection(): Array<Hero> {
//        val request = Request.Builder()
//            .url("https://api.opendota.com/api/heroes")
//            .build()
//        val response = client.newCall(request).execute()
//        val json = response.body?.string() ?: throw IllegalStateException("No response body")
//        return gson.fromJson(json, Array<Hero>::class.java)
//    }
}