package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentSettingsBinding
import com.example.apiapplication.presentation.viewmodel.SettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsFragment : Fragment(){

    private lateinit var binding : FragmentSettingsBinding
    private val viewModel : SettingsViewModel by lazy { ViewModelProvider(this)[SettingsViewModel::class.java] }
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        bottomNavigation = requireActivity().findViewById(R.id.navigation)
        bottomNavigation.visibility = View.VISIBLE
        setEventListener()
        return binding.root
    }

    private fun setEventListener() {
        binding.logoutButton.setOnClickListener {
            viewModel.logOut()
            findNavController().apply {
                //popBackStack(R.id.settingsFragment, false)
                navigate(R.id.action_settingsFragment_to_loginFragment)
                //bottomNavigation.visibility = View.INVISIBLE
            }
        }
    }
}