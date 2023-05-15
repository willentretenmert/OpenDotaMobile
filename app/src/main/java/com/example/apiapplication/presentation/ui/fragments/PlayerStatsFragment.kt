package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentPlayerstatsBinding
import com.example.apiapplication.presentation.ui.adapters.MatchesAdapter
import com.example.apiapplication.presentation.viewmodel.PlayerStatsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlayerStatsFragment : Fragment() {

    private lateinit var binding: FragmentPlayerstatsBinding
    private lateinit var viewModel: PlayerStatsViewModel
    private val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)
    private lateinit var matchesAdapter: MatchesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PlayerStatsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerstatsBinding.inflate(layoutInflater, container, false)
        bottomNavigation?.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: PlayerStatsFragmentArgs by navArgs()
        val id = args.playerId
        recyclerView = view.findViewById(R.id.matches_recycler_view)
        matchesAdapter = MatchesAdapter(emptyList(),emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = matchesAdapter
        }

        //fetching api requests
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchHeroes()
            viewModel.fetchPlayerStats(id)
            viewModel.fetchRecentMatches(id)

        }

        //view updating
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerProfile { playerStatsNickname, playerAvatarURL, playersRank ->
                binding.playerstats.playerstatsNickname.text = playerStatsNickname
                Glide.with(binding.playerstats.profileBriefing.userProfilePicture.context)
                    .load(playerAvatarURL)
                    .into(binding.playerstats.profileBriefing.userProfilePicture)
                val resId = resources.getIdentifier(playersRank, "mipmap", context?.packageName)
                binding.playerstats.playerstatsRank.setImageResource(resId)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerWRMP { playerWinrate, playersTotalMP ->
                with(binding.playerstats.profileBriefing.briefingWinrate) {
                    text = playerWinrate.toString() + "%"
                    if (playerWinrate > 50)
                        setTextColor(ContextCompat.getColor(context, R.color.winColor))
                    else setTextColor(ContextCompat.getColor(context, R.color.slightLossColor))
                }
                binding.playerstats.profileBriefing.briefingTotalMatches.text = playersTotalMP
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerHeroStats { heroIndex, heroIcon, heroGames, heroWinrate ->
                val resId = resources.getIdentifier(heroIcon, "mipmap", context?.packageName)
                when (heroIndex) {
                    0 -> {
                        binding.playerstats.hero1.heroIcon.setImageResource(resId)
                        binding.playerstats.hero1.heroGames.text = heroGames
                        binding.playerstats.hero1.heroWinrate.text = heroWinrate
                    }

                    1 -> {
                        binding.playerstats.hero2.heroIcon.setImageResource(resId)
                        binding.playerstats.hero2.heroGames.text = heroGames
                        binding.playerstats.hero2.heroWinrate.text = heroWinrate
                    }

                    2 -> {
                        binding.playerstats.hero3.heroIcon.setImageResource(resId)
                        binding.playerstats.hero3.heroGames.text = heroGames
                        binding.playerstats.hero3.heroWinrate.text = heroWinrate
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            collectRecentMatches()
        }

    }


    private suspend fun collectPlayerHeroStats(onHeroDataReady: (Int, String, String, String) -> Unit) {
        val heroes = viewModel.heroes.first { it.isNotEmpty() }
        viewModel.playersHeroStats.collect { playersHeroStats ->
            if (playersHeroStats.size >= 3) {
                for (i in 0..2) {
                    val heroName = viewModel.getHeroNameByPlayerStatsIndex(heroes, playersHeroStats, i)
                    val heroGames = viewModel.getHeroGames(playersHeroStats, i)
                    val heroWinrate = viewModel.getHeroWinrate(playersHeroStats, i)
                    onHeroDataReady(i, heroName, heroGames, heroWinrate)
                }
            }
        }
    }

    // 1068042013
    // 275690206
    // 228759689
    private suspend fun collectPlayerProfile(onPlayerDataReady: (String, String, String) -> Unit) {
        viewModel.playersProfile.collect { playersProfile ->
            val playerStatsNickname = viewModel.getPlayersPersonaName(playersProfile)
            val playerAvatarURL = viewModel.getPlayersAvatar(playersProfile)
            val playersRank = viewModel.getPlayersRank(playersProfile)
            onPlayerDataReady(playerStatsNickname, playerAvatarURL, playersRank)
        }
    }

    private suspend fun collectPlayerWRMP(onPlayerDataReady: (Int, String) -> Unit) {
        viewModel.playersWinrate.collect { PlayersWinrate ->
            val playersWinrate = viewModel.getPlayersWinrate(PlayersWinrate)
            val playersTotalMP = viewModel.getPlayersTotal(PlayersWinrate)
            onPlayerDataReady(playersWinrate, playersTotalMP)
        }
    }

    private suspend fun collectRecentMatches() {
        val heroes = viewModel.heroes.first { it.isNotEmpty() }
        viewModel.recentMatches.collect { recentMatches ->
            matchesAdapter = MatchesAdapter(heroes, recentMatches)
            recyclerView.adapter = matchesAdapter
        }
    }


}
