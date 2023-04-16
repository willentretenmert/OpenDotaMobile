package com.example.apiapplication.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchHeroes()

        with(binding) {
            button.setOnClickListener {
                if (editText.text.isNotBlank()) {
                    val id = editText.text.toString()
                    viewModel.fetchPlayerStats(id)

                    observeData { heroIndex, heroName, appendix ->
                        when (heroIndex) {
                            0 -> textView.setTextAndAppend(heroName, appendix)
                            1 -> textView2.setTextAndAppend(heroName, appendix)
                            2 -> textView3.setTextAndAppend(heroName, appendix)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.tv_err), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeData(onHeroDataReady: (Int, String, String) -> Unit) {
        viewModel.heroes.observe(viewLifecycleOwner) { data ->
            viewModel.playerStats.observe(viewLifecycleOwner) { data2 ->
                for (i in 0..2) {
                    val heroName = viewModel.getHeroNameByIndex(data, data2, i)
                    val appendix = """
                    |MP: ${data2[i].games}
                    |WR: ${
                        String.format(
                            "%.0f",
                            data2[i].win.toDouble() / data2[i].games.toDouble() * 100
                        )
                    }%""".trimMargin()
                    onHeroDataReady(i, heroName, appendix)
                }
            }
        }
    }

    private fun TextView.setTextAndAppend(text: String, appendix: String) {
        this.text = text
        this.append(appendix)
    }
}

