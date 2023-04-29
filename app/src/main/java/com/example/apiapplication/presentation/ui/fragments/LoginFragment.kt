package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val bottomNavigation =
            activity?.findViewById<BottomNavigationView>(R.id.navigation)
        bottomNavigation?.visibility = View.INVISIBLE
        setEventListener()
        return binding.root
    }

    private fun setEventListener() {
        binding.loginButton.setOnClickListener {
            if (binding.editTextLogin.text.isNotBlank() and binding.editTextPassword.text.isNotBlank()) {
                val login = binding.editTextLogin.text.toString()
                val password = binding.editTextPassword.text.toString()
                if (login == "zxc" && password == "123") {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    val bottomNavigation =
                        activity?.findViewById<BottomNavigationView>(R.id.navigation)
                    bottomNavigation?.visibility = View.VISIBLE
                }
            }
        }
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }
}