package com.example.apiapplication

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apiapplication.databinding.ActivityMainBinding
import com.example.apiapplication.hero.Hero
import com.example.apiapplication.hero.PlayerStats
import com.google.gson.Gson
import okhttp3.OkHttpClient
import kotlinx.serialization.*
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.button.setOnClickListener {
            if (binding.editText.text.isNotBlank()) {
//                val id = "275690206"
                val id = binding.editText.text
                val policy = ThreadPolicy.Builder()
                    .permitAll()
                    .build()
                StrictMode.setThreadPolicy(policy)
                // 1068042013 - my id
                // 275690206 - eugenesub id
                // old response val, don't use until multiple usages created
//                val response = URL("https://api.opendota.com/api/heroes").readText()

                val data = gson.fromJson(
                    URL("https://api.opendota.com/api/heroes").readText(),
                    Array<Hero>::class.java
                )
                val data2 = gson.fromJson(
                    URL("https://api.opendota.com/api/players/$id/heroes").readText(),
                    Array<PlayerStats>::class.java
                )

                for (i in 0..2) {
                    getHeroNameByIndex(data, data2, i)
                    if (i == 0) {
                        binding.textView.append(
                            "\nMP: " + data2[i].games.toString() + "\n" +
                                    "WR: " + String.format(
                                "%.0f",
                                data2[i].win.toDouble() / data2[i].games.toDouble() * 100
                            ) + "%"
                        )
                    }
                    if (i == 1) {
                        binding.textView2.append(
                            "\nMP: " + data2[i].games.toString() + "\n" +
                                    "WR: " + String.format(
                                "%.0f",
                                data2[i].win.toDouble() / data2[i].games.toDouble() * 100
                            ) + "%"
                        )
                    }
                    if (i == 2) {
                        binding.textView3.append(
                            "\nMP: " + data2[i].games.toString() + "\n" +
                                    "WR: " + String.format(
                                "%.0f",
                                data2[i].win.toDouble() / data2[i].games.toDouble() * 100
                            ) + "%"
                        )
                    }
                }
            } else Toast.makeText(this, getString(R.string.tv_err), Toast.LENGTH_SHORT).show()
        }

    }

    private fun getHeroNameByIndex(data: Array<Hero>, data2: Array<PlayerStats>, i: Int) {
        data.forEach { v ->
            if (v.id == data2[i].hero_id.toInt()) {
                if (i == 0)
                    binding.textView.text = v.localized_name
                if (i == 1)
                    binding.textView2.text = v.localized_name
                if (i == 2)
                    binding.textView3.text = v.localized_name
            }
        }
    }
}