package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentPlayerstatsBinding
import com.example.apiapplication.presentation.ui.adapters.MyAdapter
import com.example.apiapplication.presentation.viewmodel.MainViewModel
import com.example.apiapplication.presentation.viewmodel.PlayerStatsViewModel

class PlayerStatsFragment : Fragment()  {

    private lateinit var binding: FragmentPlayerstatsBinding
    private lateinit var viewModel: PlayerStatsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PlayerStatsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playerstats, container, false)
        binding = FragmentPlayerstatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.matchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView = requireView().findViewById(R.id.matches_recycler_view)
//        myAdapter = MyAdapter()
//        recyclerView.adapter = myAdapter
        /*val recyclerView:RecyclerView = requireView().findViewById(R.id.myRecyclerView)
        recyclerView.setAdapter(myAdapter)*/
        val args: PlayerStatsFragmentArgs by navArgs()
        val id = args.playerId
        viewModel.fetchHeroes()
        viewModel.fetchPlayerStats(id)

        with(binding) {
            observeData2 {
                playersProfile -> playerstats.playerstatsNickname.text = playersProfile
            }
        }
    }

    private fun observeData1(onHeroDataReady: (Int, String, String) -> Unit) {
        viewModel.heroes.observe(viewLifecycleOwner) { heroes ->
            viewModel.playersHeroStats.observe(viewLifecycleOwner) { playersHeroStats ->
                for (i in 0..2) {
                    val heroName = viewModel.getHeroNameByIndex(heroes, playersHeroStats, i)
                    val appendix = """
                        |
                    |MP: ${playersHeroStats[i].games}
                    |WR: ${
                        String.format(
                            "%.0f",
                            playersHeroStats[i].win.toDouble() / playersHeroStats[i].games.toDouble() * 100
                        )
                    }%""".trimMargin()
                    onHeroDataReady(i, heroName, appendix)
                }
            }
        }
    }
    private fun observeData2 (onPlayerDataReady: (String) -> Unit) {
        viewModel.playersProfile.observe(viewLifecycleOwner) { playersProfile ->
            val playerStatsNickname = viewModel.getPlayersPersonaName(playersProfile)
            onPlayerDataReady(playerStatsNickname)
        }
    }
}
