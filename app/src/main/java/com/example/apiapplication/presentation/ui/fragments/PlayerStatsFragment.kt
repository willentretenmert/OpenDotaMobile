package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentPlayerStatsBinding
import com.example.apiapplication.presentation.ui.adapters.CommentsAdapter
import com.example.apiapplication.presentation.ui.adapters.MatchesAdapter
import com.example.apiapplication.presentation.viewmodel.PlayerStatsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception

class PlayerStatsFragment : Fragment() {

    private lateinit var binding: FragmentPlayerStatsBinding
    private lateinit var viewModel: PlayerStatsViewModel
    private lateinit var matchesAdapter: MatchesAdapter
    private lateinit var matchesRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var commentsRecyclerView: RecyclerView
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
        binding = FragmentPlayerStatsBinding.inflate(layoutInflater, container, false)
        bottomNavigation?.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: PlayerStatsFragmentArgs by navArgs()
        val id = args.playerId

        matchesRecyclerView = view.findViewById(R.id.matches_recycler_view)
        matchesAdapter = MatchesAdapter(emptyList(), emptyList())
        matchesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = matchesAdapter
        }

        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view)
        commentsAdapter = CommentsAdapter(emptyList())
        commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentsAdapter
        }

        //fetching api requests
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchHeroes()
            viewModel.fetchSteamIDProfile(id)
            viewModel.fetchPlayerStats(id)
            viewModel.fetchRecentMatches(id)
        }

        //view updating
        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerAvatar { playerAvatarURL ->
                Glide.with(binding.playerstats.profileBriefing.userProfilePicture.context)
                    .load(playerAvatarURL)
                    .into(binding.playerstats.profileBriefing.userProfilePicture)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            collectPlayerProfile { playerStatsNickname, playersRank ->
                binding.playerstats.playerstatsNickname.text = playerStatsNickname
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
        viewLifecycleOwner.lifecycleScope.launch {
            collectComments()
        }

        // sending a new comment
        binding.sendBtn.setOnClickListener {
            viewModel.postComment(id, "QWERTY", binding.editTextNewComment.text.toString()) { success ->
                if (success) {
                    binding.editTextNewComment.text.clear()
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    collectComments()
                }
            }
        }

    }

    private suspend fun collectPlayerHeroStats(onHeroDataReady: (Int, String, String, String) -> Unit) {
        val heroes = viewModel.heroes.first { it.isNotEmpty() }
        viewModel.playersHeroStats.collect { playersHeroStats ->
            if (playersHeroStats.size >= 3) {
                for (i in 0..2) {
                    val heroName =
                        viewModel.getHeroNameByPlayerStatsIndex(heroes, playersHeroStats, i)
                    val heroGames = viewModel.getHeroGames(playersHeroStats, i)
                    val heroWinrate = viewModel.getHeroWinrate(playersHeroStats, i)
                    onHeroDataReady(i, heroName, heroGames, heroWinrate)
                }
            }
        }
    }

    // 1068042013 - sasha
    // 191345078 - kolya
    // 275690206 - zhenya
    // 228759689 - dima
    // 132697251 - shustr
    // 121473493 - max
    // 303880693 - vlad
    // 7145036443

    private suspend fun collectPlayerProfile(onPlayerDataReady: (String, String) -> Unit) {
        viewModel.playersProfile.collect { playersProfile ->
            val playerStatsNickname = viewModel.getPlayersPersonaName(playersProfile)
            val playersRank = viewModel.getPlayersRank(playersProfile)
            onPlayerDataReady(playerStatsNickname, playersRank)
        }
    }

    private suspend fun collectPlayerAvatar(onPlayerDataReady: (String) -> Unit) {
        viewModel.playersProfile.collect { playersProfile ->
            val playerAvatarURL = viewModel.getPlayersAvatar(playersProfile)
            onPlayerDataReady(playerAvatarURL)
        }
    }

    private suspend fun collectPlayerWRMP(onPlayerDataReady: (Int, String) -> Unit) {
        viewModel.playersWinRate.collect { PlayersWinrate ->
            val playersWinrate = viewModel.getPlayersWinrate(PlayersWinrate)
            val playersTotalMP = viewModel.getPlayersTotal(PlayersWinrate)
            onPlayerDataReady(playersWinrate, playersTotalMP)
        }
    }

    private suspend fun collectRecentMatches() {
        val heroes = viewModel.heroes.first { it.isNotEmpty() }
        viewModel.recentMatches.collect { recentMatches ->
            matchesAdapter = MatchesAdapter(heroes, recentMatches)
            matchesRecyclerView.adapter = matchesAdapter
        }
    }

    private suspend fun collectComments() {
        try {
            viewModel.steamComments.collect { comments ->
                if (comments.isNotEmpty()) {
                    commentsAdapter.updateData(comments.reversed())
                    binding.tvCommentstv.text = "Comments (${commentsAdapter.itemCount}):"
                }
            }
        } catch (e: Exception) {
            Log.e("zxc", e.toString())
        }
    }
}
