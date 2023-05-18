package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.apiapplication.R
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.data.models.PlayersWinrate
import com.example.apiapplication.databinding.FragmentFavouritesBinding
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.example.apiapplication.presentation.viewmodel.FavouritesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by lazy { ViewModelProvider(this)[FavouritesViewModel::class.java] }
    private val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)

    private val auth = MainActivity.User.auth



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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchFirebaseProfile(auth.currentUser!!.email.toString())
            viewModel.fetchHeroes()
        }
        viewLifecycleOwner.lifecycleScope.launch() {
            collectFavourites()
        }
    }
    private suspend fun collectFavourites() {
        val favouritesPlayers = viewModel.favouritesPlayers.first() {it.isNotEmpty()}
        val favouriteMatches = viewModel.favouritesMatches.first() {it.isNotEmpty()}

        var playersHeroStats = viewModel.playersHeroStats.value
        var playersProfile = viewModel.playersProfile.value
        var playersWinrate = viewModel.playersWinrate.value

        for (item in favouritesPlayers) {
            viewModel.fetchPlayerStats(item.toString())
            playersHeroStats = viewModel.playersHeroStats.value
            playersProfile = viewModel.playersProfile.value
            playersWinrate = viewModel.playersWinrate.value
        }
    }
}