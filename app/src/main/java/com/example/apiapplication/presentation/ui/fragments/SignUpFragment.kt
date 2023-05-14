package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentLoginBinding
import com.example.apiapplication.databinding.FragmentSignUpBinding
import com.example.apiapplication.presentation.viewmodel.LoginViewModel
import com.example.apiapplication.presentation.viewmodel.SignUpViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var binding : FragmentSignUpBinding
    private val viewModel : SignUpViewModel by lazy { ViewModelProvider(this)[SignUpViewModel::class.java] }
    private val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        bottomNavigation?.visibility = View.INVISIBLE
        setEventListener()
        return binding.root
    }

    private fun setEventListener() {
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
        binding.signupButton.setOnClickListener {
            if (binding.editTextLogin.text.isNotBlank() and binding.editTextPassword.text.isNotBlank() and binding.editTextPasswordAgain.text.isNotBlank()) {
                if (binding.editTextPassword.text.toString() == binding.editTextPasswordAgain.text.toString()) {
                    val login = binding.editTextLogin.text.toString()
                    val password = binding.editTextPassword.text.toString()
                    viewModel.signUp(login, password, context)
                }
                else Toast.makeText(context, "passwords don't match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
