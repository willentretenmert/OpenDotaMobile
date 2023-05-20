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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.models.FirebaseUserRaspberry
import com.example.apiapplication.databinding.FragmentMatchStatsBinding
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.example.apiapplication.presentation.ui.adapters.MatchHeroesAdapter
import com.example.apiapplication.presentation.ui.adapters.PlayersAdapter
import com.example.apiapplication.presentation.viewmodel.MatchStatsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchStatsFragment : Fragment() {

    private lateinit var viewModel: MatchStatsViewModel
    private lateinit var binding: FragmentMatchStatsBinding


    private lateinit var matchHeroesRadiantAdapter: MatchHeroesAdapter
    private lateinit var matchHeroesDireAdapter: MatchHeroesAdapter

    private lateinit var matchHeroesRadiantRecyclerView: RecyclerView
    private lateinit var matchHeroesDireRecyclerView: RecyclerView

    private lateinit var playersRadiantAdapter: PlayersAdapter
    private lateinit var playersDireAdapter: PlayersAdapter

    private lateinit var playersRadiantRecyclerView: RecyclerView
    private lateinit var playersDireRecyclerView: RecyclerView

    private val auth = MainActivity.User.auth
    private val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MatchStatsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchStatsBinding.inflate(layoutInflater, container, false)
        bottomNavigation?.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MatchStatsFragmentArgs by navArgs()
        val id = args.playerId

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchHeroes()
            viewModel.fetchMatchStats(id)
        }


        matchHeroesRadiantRecyclerView =
            view.findViewById(R.id.match_briefing_radiant_hero_recycler_view)
        matchHeroesRadiantAdapter = MatchHeroesAdapter(emptyList(), emptyList())
        matchHeroesRadiantRecyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = getNonScrollableLayoutManager(LinearLayoutManager.HORIZONTAL)
            adapter = matchHeroesRadiantAdapter
        }

        matchHeroesDireRecyclerView =
            view.findViewById(R.id.match_briefing_dire_hero_recycler_view)
        matchHeroesDireAdapter = MatchHeroesAdapter(emptyList(), emptyList())
        matchHeroesDireRecyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = getNonScrollableLayoutManager(LinearLayoutManager.HORIZONTAL)
            adapter = matchHeroesDireAdapter
        }

        playersRadiantRecyclerView = view.findViewById(R.id.radiant_team_recycler_view)
        playersRadiantAdapter = PlayersAdapter(emptyList(), emptyList())
        playersRadiantRecyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = getNonScrollableLayoutManager()
            adapter = playersRadiantAdapter
        }
        playersDireRecyclerView = view.findViewById(R.id.dire_team_recycler_view)
        playersDireAdapter = PlayersAdapter(emptyList(), emptyList())
        playersDireRecyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = getNonScrollableLayoutManager()
            adapter = playersDireAdapter
        }

// 7137640948
        binding.matchBriefing.matchstatsMatchId.text = id

        viewLifecycleOwner.lifecycleScope.launch {
            collectMatchStats { matchStatsDate, matchStatsDuration, matchStatsOutcome ->
                binding.matchBriefing.matchstatsMatchDate.text = matchStatsDate
                binding.matchBriefing.matchBriefingTimer.text = matchStatsDuration
                binding.matchBriefing.matchBriefingRadiantWin.text = ""
                binding.matchBriefing.matchBriefingDireWin.text = ""
                if (matchStatsOutcome != null) {
                    if (matchStatsOutcome) {
                        binding.matchBriefing.matchBriefingRadiantWin.text = "Radiant Victory"
                    } else {
                        binding.matchBriefing.matchBriefingDireWin.text = "Dire Victory"
                    }
                }
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            collectMatchPlayers()
        }

        var isFavourite: Boolean


        viewLifecycleOwner.lifecycleScope.launch {
            val favouritesMatchesFlow = viewModel.favouritesMatches.stateIn(this)
            favouritesMatchesFlow.collect { match ->
                isFavourite = match.contains(FirebaseUserRaspberry.FavouriteMatches(id))
                if (!isFavourite) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            binding.matchBriefing.addMatchToFavsBtn.setBackgroundResource(
                                R.drawable.button_like
                            )
                            binding.matchBriefing.addMatchToFavsBtn.setImageResource(
                                R.drawable.ic_like
                            )
                        }
                    }
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            binding.matchBriefing.addMatchToFavsBtn.setBackgroundResource(
                                R.drawable.button_delete
                            )
                            binding.matchBriefing.addMatchToFavsBtn.setImageResource(
                                R.drawable.ic_delete
                            )
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    binding.matchBriefing.addMatchToFavsBtn.setOnClickListener {
                        if (isFavourite) {
                            viewModel.deleteFavouriteMatch(
                                auth.currentUser?.email.toString(),
                                "QWERTY",
                                id
                            ) { success ->
                                if (success) {
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        withContext(Dispatchers.Main) {
                                            binding.matchBriefing.addMatchToFavsBtn.setBackgroundResource(
                                                R.drawable.button_like
                                            )
                                            binding.matchBriefing.addMatchToFavsBtn.setImageResource(
                                                R.drawable.ic_like
                                            )
                                            isFavourite = false
                                        }
                                    }
                                } else {
                                    Log.e("zxc", "Failed to delete favourite player")
                                }
                            }
                        } else {
                            viewModel.postFavouriteMatch(
                                auth.currentUser?.email.toString(),
                                "QWERTY",
                                id
                            ) { success ->
                                if (success) {
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        withContext(Dispatchers.Main) {
                                            binding.matchBriefing.addMatchToFavsBtn.setBackgroundResource(
                                                R.drawable.button_delete
                                            )
                                            binding.matchBriefing.addMatchToFavsBtn.setImageResource(
                                                R.drawable.ic_delete
                                            )
                                            isFavourite = true
                                        }
                                    }
                                } else {
                                    Log.e("zxc", "Failed to add favourite player")
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun getNonScrollableLayoutManager(orientation: Int = LinearLayoutManager.VERTICAL): LinearLayoutManager {
        return object : LinearLayoutManager(requireContext(), orientation, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
    }

    private suspend fun collectMatchStats(onMatchStatsDataReady: (String, String, Boolean?) -> Unit) {
        viewModel.matchStats.collect { matchStats ->
            val matchStatsDate = viewModel.getMatchStartDate(matchStats)
            val matchStatsDurationHMS = viewModel.getMatchDurationHMS(matchStats)
            val matchStatsOutcome = viewModel.getOutcome(matchStats)

            onMatchStatsDataReady(matchStatsDate, matchStatsDurationHMS, matchStatsOutcome)

        }
    }

    private suspend fun collectMatchPlayers() {

        val heroes = viewModel.heroes.first { it.isNotEmpty() }
        val players = viewModel.players.first { it.isNotEmpty() }

        if (players.size >= 10) {
            val radiantTeam = players.subList(0, 5)
            val direTeam = players.subList(5, 10)

            matchHeroesRadiantAdapter.updateData(heroes, radiantTeam)
            matchHeroesDireAdapter.updateData(heroes, direTeam)

            playersRadiantAdapter.updateData(heroes, radiantTeam)
            playersDireAdapter.updateData(heroes, direTeam)

            playersRadiantAdapter.onItemClick = { item ->
                val action =
                    MatchStatsFragmentDirections.actionMatchStatsFragmentToPlayerStatsFragment(item.toString())
                findNavController().navigate(action)
            }
            playersDireAdapter.onItemClick = { item ->
                val action =
                    MatchStatsFragmentDirections.actionMatchStatsFragmentToPlayerStatsFragment(item.toString())
                findNavController().navigate(action)
            }
        }
    }
}

