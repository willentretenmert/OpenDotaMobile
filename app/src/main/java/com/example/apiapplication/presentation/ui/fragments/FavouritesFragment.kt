package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentFavouritesBinding
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.example.apiapplication.presentation.ui.adapters.FavouritesPlayersAdapter
import com.example.apiapplication.presentation.viewmodel.FavouritesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by lazy { ViewModelProvider(this)[FavouritesViewModel::class.java] }
    private val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)
    private lateinit var favouritesPlayersAdapter: FavouritesPlayersAdapter
    private lateinit var favouritesPlayersRecyclerView: RecyclerView
    private val auth = MainActivity.User.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        bottomNavigation?.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesPlayersRecyclerView = view.findViewById(R.id.favourites_players_recycler_view)
        favouritesPlayersAdapter =
            FavouritesPlayersAdapter(emptyList(), mutableListOf(), mutableListOf(), mutableListOf())
        favouritesPlayersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouritesPlayersAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchFirebaseProfile(auth.currentUser!!.email.toString())
            viewModel.fetchHeroes()
        }
        viewLifecycleOwner.lifecycleScope.launch() {
            collectFavouritesPlayers()
        }
    }

    private suspend fun collectFavouritesPlayers() {
        try {
            val heroes = viewModel.heroes.first { it.isNotEmpty() }
            viewModel.favouritesPlayers.collect { players ->
                if (players.isNotEmpty()) {
                    for (item in players) {
                        viewModel.fetchPlayerStats(item.steam_id)
                        favouritesPlayersAdapter.updateData(heroes)
                        delay(2500)
                        val profile = viewModel.playersProfile.first { it != null }
                        val heroStats = viewModel.playersHeroStats.first { it.isNotEmpty() }
                        val winrate = viewModel.playersWinrate.first { it != null }

                        if (profile != null && winrate != null) {
                            favouritesPlayersAdapter.addData(
                                profile, heroStats, winrate
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("zxc", e.toString())
        }
    }


//  TODO      viewModel.favouritesMatches.first() { it.isNotEmpty() }


}