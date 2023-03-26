package com.example.apiapplication.presentation.ui

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apiapplication.R
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.PlayerStats
import com.example.apiapplication.databinding.FragmentMainBinding
import com.google.gson.Gson
import java.net.URL

class MainFragment : Fragment() {

    private val gson = Gson()
    //private val _binding : FragmentMainBinding? = null
    //private val binding get() = _binding!!
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            if (binding.editText.text.isNotBlank()) {
                val id = binding.editText.text
                val policy = StrictMode.ThreadPolicy.Builder()
                    .permitAll()
                    .build()
                StrictMode.setThreadPolicy(policy)
                // 1068042013 - my id
                // 275690206 - eugenesub id
                // old response val, don't use until multiple usages created
                //val response = URL("https://api.opendota.com/api/heroes").readText()

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
                    val appendix: String = "\nMP: " + data2[i].games.toString() + "\n" +
                            "WR: " + String.format("%.0f", data2[i].win.toDouble() / data2[i].games.toDouble() * 100) + "%"
                    when(i) {
                        0 -> binding.textView.append(appendix)
                        1 -> binding.textView2.append(appendix)
                        2 -> binding.textView3.append(appendix)
                    }
                }
            } else Toast.makeText(requireContext(), getString(R.string.tv_err), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getHeroNameByIndex(data: Array<Hero>, data2: Array<PlayerStats>, i: Int) {
        data.forEach { v ->
            if (v.id == data2[i].hero_id.toInt()) {
                when(i) {
                    0 -> binding.textView.text = v.localized_name
                    1 -> binding.textView2.text = v.localized_name
                    2 -> binding.textView3.text = v.localized_name
                }
            }
        }
    }
}

