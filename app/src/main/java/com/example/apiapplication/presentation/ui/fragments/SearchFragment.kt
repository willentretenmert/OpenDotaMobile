package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentSearchBinding
import com.example.apiapplication.presentation.viewmodel.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private var isBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)
        bottomNavigation?.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchPlayerBtn.setOnClickListener {
            if (binding.searchBar.text.trim().isNotBlank()) {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToPlayerStatsFragment( binding.searchBar.text.toString() )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), getString(R.string.tv_err), Toast.LENGTH_SHORT).show()
            }
        }
        binding.searchMatchBtn.setOnClickListener {
            if (binding.searchBar.text.trim().isNotBlank()) {
                val action = SearchFragmentDirections.actionSearchFragmentToMatchStatsFragment( binding.searchBar.text.toString() )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), getString(R.string.tv_err), Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun handleOnBackPressed() {
        if (isBackPressed) {
            requireActivity().finish()
        } else {
            isBackPressed = true
            Toast.makeText(
                requireContext(),
                "Нажмите еще раз, чтобы выйти",
                Toast.LENGTH_SHORT
            )
                .show()
            Handler(Looper.getMainLooper())
                .postDelayed({ isBackPressed = false }, 2000)
        }
    }
}
