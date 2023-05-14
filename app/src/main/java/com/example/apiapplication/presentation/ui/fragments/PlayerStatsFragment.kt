package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentPlayerstatsBinding
import com.example.apiapplication.presentation.viewmodel.PlayerStatsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerStatsFragment : Fragment() {

    private lateinit var binding: FragmentPlayerstatsBinding
    private lateinit var viewModel: PlayerStatsViewModel
    private val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.navigation)

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchHeroes()
            viewModel.fetchPlayerStats(id)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerProfile { playerStatsNickname ->
                binding.playerstats.playerstatsNickname.text = playerStatsNickname
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerWinrate { playerWinrate ->
                binding.playerstats.profileBriefing.briefingWinrate.text = playerWinrate
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerTotalMP { playersTotalMP ->
                binding.playerstats.profileBriefing.briefingTotalMatches.text =
                    playersTotalMP
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerHeroStats { heroIndex, heroName, heroGames, heroWinrate ->
                when (heroIndex) {
                    0 -> {
                        binding.playerstats.hero1.heroGames.text = heroGames
                        binding.playerstats.hero1.heroWinrate.text = heroWinrate
                    }

                    1 -> {
                        binding.playerstats.hero2.heroGames.text = heroGames
                        binding.playerstats.hero2.heroWinrate.text = heroWinrate
                    }

                    2 -> {
                        binding.playerstats.hero3.heroGames.text = heroGames
                        binding.playerstats.hero3.heroWinrate.text = heroWinrate
                    }
                }
            }
        }
    }


    private suspend fun collectPlayerHeroStats(onHeroDataReady: (Int, String, String, String) -> Unit) {
        viewModel.heroes.collect { heroes ->
            viewModel.playersHeroStats.collect { playersHeroStats ->
                if (playersHeroStats.size >= 3) {
                    for (i in 0..2) {
                        val heroName = viewModel.getHeroNameByIndex(heroes, playersHeroStats, i)
                        val heroGames = viewModel.getHeroGames(playersHeroStats, i)
                        val heroWinrate = viewModel.getHeroWinrate(playersHeroStats, i)
                        onHeroDataReady(i, heroName, heroGames, heroWinrate)
                    }
                }
            }
        }
    }

    private suspend fun collectPlayerProfile(onPlayerDataReady: (String) -> Unit) {
        viewModel.playersProfile.collect { playersProfile ->
            val playerStatsNickname = viewModel.getPlayersPersonaName(playersProfile)
            onPlayerDataReady(playerStatsNickname)
        }
    }

    private suspend fun collectPlayerWinrate(onPlayerDataReady: (String) -> Unit) {
        viewModel.playersWinrate.collect { PlayersWinrate ->
            val playersWinrate = viewModel.getPlayersWinrate(PlayersWinrate)
            onPlayerDataReady(playersWinrate)
        }
    }

    private suspend fun collectPlayerTotalMP(onPlayerDataReady: (String) -> Unit) {
        viewModel.playersWinrate.collect { PlayersTotalMP ->
            val playersTotalMP = viewModel.getPlayersTotal(PlayersTotalMP)
            onPlayerDataReady(playersTotalMP)
        }
    }

}
