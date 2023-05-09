package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentLoginBinding
import com.example.apiapplication.presentation.viewmodel.LoginViewModel
import com.example.apiapplication.presentation.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val viewModel : LoginViewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    val mAuth = FirebaseAuth.getInstance()

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
        if (!viewModel.checkAuth()){
            binding.loginButton.setOnClickListener {
                if (binding.editTextLogin.text.isNotBlank() and binding.editTextPassword.text.isNotBlank()) {
                    val login = binding.editTextLogin.text.toString()
                    val password = binding.editTextPassword.text.toString()
                    viewModel.getAuth(login, password) { loginResult ->
                        if (loginResult)
                        {
                            findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
                            val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)
                            bottomNavigation?.visibility = View.VISIBLE
                        }
                        else Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else {
            findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
            val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)
            bottomNavigation?.visibility = View.VISIBLE
        }
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }
}