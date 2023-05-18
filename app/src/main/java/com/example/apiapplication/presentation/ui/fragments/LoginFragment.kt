package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentLoginBinding
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.example.apiapplication.presentation.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    private var login: String? = null
    private var password: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)
        bottomNavigation?.visibility = View.INVISIBLE

        try {
            val args: LoginFragmentArgs by navArgs()
            login = args.login
            password = args.password
            binding.editTextLogin.setText(login)
            binding.editTextPassword.setText(password)
        } finally {

        }
        viewModel.checkAuth()
        setEventListener()
        viewLifecycleOwner.lifecycleScope.launch { setObserver() }
        return binding.root
    }

    private fun setEventListener() {
        binding.signupButton.setOnClickListener() {findNavController().navigate(R.id.action_loginFragment_to_signupFragment)}
        binding.loginButton.setOnClickListener {
            if (binding.editTextLogin.text.isNotBlank() and binding.editTextPassword.text.isNotBlank()) {
                val login = binding.editTextLogin.text.toString()
                val password = binding.editTextPassword.text.toString()
                binding.editTextLogin.text.clear()
                binding.editTextPassword.text.clear()
                viewModel.getAuth(login, password) { loginResult ->
                    if (loginResult) findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
                    else Toast.makeText(context,"Authentication failed.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private suspend fun setObserver() {
        viewModel.isLoginSuccessful.collect() {
            if (it == true) {
                findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
            }
        }
    }
}
