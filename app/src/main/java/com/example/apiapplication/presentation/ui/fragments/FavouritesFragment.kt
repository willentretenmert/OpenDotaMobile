package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentFavouritesBinding
import com.example.apiapplication.databinding.FragmentLoginBinding
import com.example.apiapplication.presentation.viewmodel.FavouritesViewModel
import com.example.apiapplication.presentation.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {

    private lateinit var binding : FragmentFavouritesBinding
    private val viewModel : FavouritesViewModel by lazy { ViewModelProvider(this)[FavouritesViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        //viewLifecycleOwner.lifecycleScope.launch { setObserver() }
        return binding.root
    }
}