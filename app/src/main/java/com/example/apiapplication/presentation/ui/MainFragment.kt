package com.example.apiapplication.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentMainBinding
import com.example.apiapplication.presentation.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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

        viewModel.fetchHeroes()

        binding.button.setOnClickListener {
            if (binding.editText.text.isNotBlank()) {
                val id = binding.editText.text.toString()
                viewModel.fetchPlayerStats(id)

                viewModel.heroes.observe(viewLifecycleOwner) { data ->
                    viewModel.playerStats.observe(viewLifecycleOwner) { data2 ->
                        for (i in 0..2) {
                            val heroName = viewModel.getHeroNameByIndex(data, data2, i)
                            val appendix: String =
                                "\nMP: " + data2[i].games.toString() + "\n" +
                                        "WR: " + String.format(
                                    "%.0f",
                                    data2[i].win.toDouble() / data2[i].games.toDouble() * 100
                                ) + "%"
                            when (i) {
                                0 -> {
                                    binding.textView.text = heroName
                                    binding.textView.append(appendix)
                                }
                                1 -> {
                                    binding.textView2.text = heroName
                                    binding.textView2.append(appendix)
                                }
                                2 -> {
                                    binding.textView3.text = heroName
                                    binding.textView3.append(appendix)
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.tv_err), Toast.LENGTH_SHORT).show()
            }
        }
    }
}

