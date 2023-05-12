package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.apiapplication.databinding.FragmentPlayerstatsBinding
import com.example.apiapplication.presentation.viewmodel.PlayerStatsViewModel
import kotlinx.coroutines.launch

class PlayerStatsFragment : Fragment()  {

    private lateinit var binding: FragmentPlayerstatsBinding
    private lateinit var viewModel: PlayerStatsViewModel

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: PlayerStatsFragmentArgs by navArgs()
        val id = args.playerId
        viewModel.fetchHeroes()
        viewModel.fetchPlayerStats(id)

        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerProfile { playerStatsNickname ->
                binding.playerstats.playerstatsNickname.text = playerStatsNickname }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerWinrate { playerWinrate ->
            binding.playerstats.profileBriefing.briefingWinrate.text = playerWinrate }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerTotalMP { playersTotalMP ->
            binding.playerstats.profileBriefing.briefingTotalMatches.text = playersTotalMP }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerHeroStats { heroName, heroGames, heroWinrate ->
            binding.playerstats.hero1.heroGames.text = heroGames
            binding.playerstats.hero1.heroWinrate.text = heroWinrate }
        }



    }

    private suspend fun collectPlayerHeroStats(onHeroDataReady: (String, String, String) -> Unit) {
        viewModel.heroes.collect { heroes ->
            viewModel.playersHeroStats.collect { playersHeroStats ->
                val heroName = viewModel.getHeroNameByIndex(heroes, playersHeroStats)
                val heroGames = viewModel.getHeroGames(playersHeroStats)
                val heroWinrate = viewModel.getHeroWinrate(playersHeroStats)
                onHeroDataReady(heroName, heroGames, heroWinrate)
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
