package com.example.apiapplication.presentation.ui.adapters

import android.content.Context
import android.graphics.RadialGradient
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.databinding.ItemMatchBriefingRvBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavouritesMatchesAdapter(
    private var dataHero: List<Hero>,
    private var dataSetMatches: MutableList<MatchStats>,
    private var dataSetRadiant: MutableList<List<MatchStats.Player>>,
    private var dataSetDire: MutableList<List<MatchStats.Player>>
) : RecyclerView.Adapter<FavouritesMatchesAdapter.FavouritesMatchesViewHolder>() {


    var onItemClick: ((Long) -> Unit)? = null
    var onItemClick2: ((Long) -> Unit)? = null

    inner class FavouritesMatchesViewHolder(val binding: ItemMatchBriefingRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(dataSetMatches[adapterPosition].match_id)
            }
            binding.addMatchToFavsBtn.apply {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        setBackgroundResource(R.drawable.button_delete)
                        setImageResource(R.drawable.ic_delete)
                        setOnClickListener {
                            onItemClick2?.invoke(dataSetMatches[adapterPosition].match_id)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesMatchesViewHolder {
        val binding =
            ItemMatchBriefingRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouritesMatchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouritesMatchesViewHolder, position: Int) {
        val currentMatch = dataSetMatches[position]
        val outcome = getOutcome(currentMatch)

        with(holder.binding) {
            matchBriefingRadiantWin
            if (outcome != null) {
                if (outcome) {
                    matchBriefingRadiantWin.text = "Radiant Victory"
                } else {
                    matchBriefingDireWin.text = "Dire Victory"
                }
            }

            matchBriefingTimer.text = getMatchDurationHMS(currentMatch)
            matchstatsMatchDate.text = getMatchStartDate(currentMatch)
            matchstatsMatchId.text = currentMatch.match_id.toString()

            for (i in 0..9) {
                val resId = getResourceId(holder.itemView.context, getHeroNameByPlayerIndex(dataHero, currentMatch.players, i))
                when (i) {
                    0 -> matchRadiantHero1.matchBriefingHeroIcon.setImageResource(resId)
                    1 -> matchRadiantHero2.matchBriefingHeroIcon.setImageResource(resId)
                    2 -> matchRadiantHero3.matchBriefingHeroIcon.setImageResource(resId)
                    3 -> matchRadiantHero4.matchBriefingHeroIcon.setImageResource(resId)
                    4 -> matchRadiantHero5.matchBriefingHeroIcon.setImageResource(resId)
                    5 -> matchDireHero1.matchBriefingHeroIcon.setImageResource(resId)
                    6 -> matchDireHero2.matchBriefingHeroIcon.setImageResource(resId)
                    7 -> matchDireHero3.matchBriefingHeroIcon.setImageResource(resId)
                    8 -> matchDireHero4.matchBriefingHeroIcon.setImageResource(resId)
                    9 -> matchDireHero5.matchBriefingHeroIcon.setImageResource(resId)
                }
            }
        }

    }

    override fun getItemCount() = dataSetMatches.size

    fun updateData(
        newHeroes: List<Hero>,
    ) {
        this.dataHero = newHeroes

        notifyDataSetChanged()
    }

    fun removeData(
        oldMatch: MatchStats,
        oldRadiant: List<MatchStats.Player>,
        oldDire: List<MatchStats.Player>
    ) {
        this.dataSetMatches.remove(oldMatch)
        this.dataSetRadiant.remove(oldRadiant)
        this.dataSetDire.remove(oldDire)
        notifyItemRemoved(dataSetMatches.size + 1)
    }

    fun addData(
        newMatches: MatchStats,
        newRadiant: List<MatchStats.Player>,
        newDire: List<MatchStats.Player>,
    ) {
        this.dataSetMatches.add(newMatches)
        this.dataSetRadiant.add(newRadiant)
        this.dataSetDire.add(newDire)
        notifyItemInserted(dataSetMatches.size - 1)
    }

    fun clearData() {
        dataSetMatches.clear()
        dataSetRadiant.clear()
        dataSetDire.clear()
        notifyDataSetChanged()
    }

    private fun getHeroNameByPlayerIndex(
        data: List<Hero>,
        data2: List<MatchStats.Player>,
        i: Int
    ): String {
        val name = data.firstOrNull { it.id == data2[i].hero_id }?.name ?: ""
        return "ic_${name}"
    }

    fun getMatchStartDate(data: MatchStats?): String {
        val date = Date((data?.start_time ?: 0) * 1000L)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getMatchDurationHMS(data: MatchStats?): String {
        val hours = (data?.duration ?: 0) / 3600
        val minutes = ((data?.duration ?: 0) % 3600) / 60
        val seconds = (data?.duration ?: 0) % 60
        return if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, seconds)
        else String.format("%02d:%02d", minutes, seconds)
    }

    fun getOutcome(data: MatchStats?): Boolean? {
        return data?.radiant_win
    }

    private fun getResourceId(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "mipmap", context.packageName)
    }

}
