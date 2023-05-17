package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentFavouritesBinding
import com.example.apiapplication.presentation.viewmodel.FavouritesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by lazy { ViewModelProvider(this)[FavouritesViewModel::class.java] }
    private val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        bottomNavigation?.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}