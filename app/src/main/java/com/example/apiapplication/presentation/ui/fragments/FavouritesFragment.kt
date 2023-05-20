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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentFavouritesBinding
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.example.apiapplication.presentation.ui.adapters.FavouritesMatchesAdapter
import com.example.apiapplication.presentation.ui.adapters.FavouritesPlayersAdapter
import com.example.apiapplication.presentation.viewmodel.FavouritesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by lazy { ViewModelProvider(this)[FavouritesViewModel::class.java] }
    private val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)

    private lateinit var favouritesPlayersAdapter: FavouritesPlayersAdapter
    private lateinit var favouritesPlayersRecyclerView: RecyclerView

    private lateinit var favouritesMatchesAdapter: FavouritesMatchesAdapter
    private lateinit var favouritesMatchesRecyclerView: RecyclerView


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
        favouritesMatchesRecyclerView = view.findViewById(R.id.favourites_matches_recycler_view)

        favouritesPlayersAdapter =
            FavouritesPlayersAdapter(emptyList(), mutableListOf(), mutableListOf(), mutableListOf())
        favouritesPlayersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouritesPlayersAdapter
        }

        favouritesMatchesAdapter =
            FavouritesMatchesAdapter(emptyList(), mutableListOf(), mutableListOf(), mutableListOf())
        favouritesMatchesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouritesMatchesAdapter
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchHeroes()
            viewModel.fetchFirebaseProfile(auth.currentUser!!.email.toString())
        }
        viewLifecycleOwner.lifecycleScope.launch() {
            collectFavouritesPlayers()
        }
        viewLifecycleOwner.lifecycleScope.launch() {
            collectFavouritesMatches()
        }
    }

    private suspend fun collectFavouritesPlayers() {
        try {
            val heroes = viewModel.heroes.first { it.isNotEmpty() }
            viewModel.favouritesPlayers.collect { players ->
                if (players.isNotEmpty()) {
                    for (item in players) {
                        viewModel.fetchPlayerStats(item.steam_id).collect {
                            favouritesPlayersAdapter.updateData(
                                heroes,
                                mutableListOf(),
                                mutableListOf(),
                                mutableListOf()
                            )
                            val profile = viewModel.playersProfile.first { it != null }
                            val heroStats = viewModel.playersHeroStats.first { it.isNotEmpty() }
                            val winrate = viewModel.playersWinrate.first { it != null }

                            if (profile != null && winrate != null) {
                                favouritesPlayersAdapter.addData(
                                    profile, heroStats, winrate
                                )
                            }
                            favouritesPlayersAdapter.onItemClick = { item ->
                                val action =
                                    FavouritesFragmentDirections.actionFavouritesFragmentToPlayerStatsFragment(
                                        item.toString()
                                    )
                                findNavController().navigate(action)
                            }
                            favouritesPlayersAdapter.onItemClick2 = { item ->
                                viewModel.deleteFavouritePlayer(
                                    auth.currentUser?.email.toString(),
                                    "QWERTY",
                                    item.toString()
                                ) { success ->
                                    Log.d("zxc", "player $item deleted: $success")
                                    if (profile != null && winrate != null) {
                                        favouritesPlayersAdapter.removeData(
                                            profile,
                                            heroStats,
                                            winrate
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("zxc", e.toString())
        }
    }

    private suspend fun collectFavouritesMatches() {
        try {
            val heroes = viewModel.heroes.first { it.isNotEmpty() }
            viewModel.favouritesMatches.collect { matches ->
                if (matches.isNotEmpty()) {
                    for (item in matches) {
                        viewModel.fetchMatchStats(item.match_id).collect {
                            val players = viewModel.players.first { it.isNotEmpty() }
                            if (players.size >= 10) {
                                favouritesMatchesAdapter.updateData(
                                    heroes,
                                    mutableListOf(), mutableListOf(), mutableListOf()
                                )
                                val matchStats = viewModel.matchStats.first { it != null }
                                val playersFromPlayers = viewModel.players.first { it.isNotEmpty() }
                                val radiantTeam = playersFromPlayers.subList(0, 5)
                                val direTeam = playersFromPlayers.subList(5, 10)

                                if (matchStats != null) {
                                    favouritesMatchesAdapter.addData(
                                        matchStats, radiantTeam, direTeam
                                    )
                                }
                                favouritesMatchesAdapter.onItemClick = { item ->
                                    val action =
                                        FavouritesFragmentDirections.actionFavouritesFragmentToMatchStatsFragment(
                                            item.toString()
                                        )
                                    findNavController().navigate(action)
                                }
                                favouritesMatchesAdapter.onItemClick2 = { item ->
                                    viewModel.deleteFavouriteMatch(
                                        auth.currentUser?.email.toString(),
                                        "QWERTY",
                                        item.toString()
                                    ) { success ->
                                        Log.d("zxc", "player $item deleted: $success")
                                        if (matchStats != null) {
                                            favouritesMatchesAdapter.removeData(
                                                matchStats, radiantTeam, direTeam
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("zxc", e.toString())
        }
    }
}