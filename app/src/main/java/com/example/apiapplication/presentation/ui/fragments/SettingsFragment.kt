package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentSettingsBinding
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.example.apiapplication.presentation.viewmodel.SettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class SettingsFragment : Fragment(){

    private lateinit var binding : FragmentSettingsBinding
    private val viewModel : SettingsViewModel by lazy { ViewModelProvider(this)[SettingsViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)
        bottomNavigation?.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchFirebaseProfile(MainActivity.User.auth.currentUser!!.email.toString())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.firebaseProfile.collect() { profile ->
                val nickname = profile?._name
                binding.email.setText(MainActivity.User.auth.currentUser!!.email.toString())
                binding.editNickname.setText(nickname)
            }
        }

        setEventListener()
        return binding.root
    }

    private fun setEventListener() {
        binding.logoutButton.setOnClickListener {
            viewModel.logOut()
            findNavController().apply {
                navigate(R.id.action_settingsFragment_to_loginFragment)
                popBackStack(R.id.settingsFragment, true)
            }
        }
        binding.editProfileButton.setOnClickListener {
            if (binding.editNickname.text.isNotBlank()) {
                viewModel.updateNickname(MainActivity.User.auth.currentUser!!.email.toString(),binding.editNickname.text.toString()) { success ->
                    Log.d("zxc", "good")
                }
            }
        }
    }
}