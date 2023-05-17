package com.example.apiapplication.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.RecentMatches


class MatchesAdapter(
    private val dataSetHeroes: List<Hero>,
    private val dataSet: List<RecentMatches>
) :
    RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>() {

    class MatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val heroIcon: ImageView = itemView.findViewById(R.id.match_hero_icon)
        val outcome: TextView = itemView.findViewById(R.id.match_outcome)
        val score: TextView = itemView.findViewById(R.id.match_score)
        val mode: TextView = itemView.findViewById(R.id.match_mode)
        val duration: TextView = itemView.findViewById(R.id.match_duration)

        init {
            itemView.setOnClickListener { }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recent_match, parent, false)
        return MatchesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        val currentItem = dataSet[position]

        val heroName = getHeroNameByRecentMatchesIndex(dataSetHeroes, dataSet, position)
        val resId = getResourceId(holder.itemView.context, heroName)

        holder.heroIcon.setImageResource(resId)
        holder.outcome.text = getOutcome(holder, currentItem.player_slot, currentItem.radiant_win)
        holder.score.text = "${currentItem.kills}/${currentItem.deaths}/${currentItem.assists}"
        holder.mode.text = getGamemodeName(currentItem.game_mode)
        holder.duration.text = getDurationHMS(currentItem.duration)
    }

    override fun getItemCount() = dataSet.size

    private fun getHeroNameByRecentMatchesIndex(
        data: List<Hero>,
        data2: List<RecentMatches>,
        i: Int
    ): String {
        val name = data.firstOrNull { it.id == data2[i].hero_id }?.name ?: ""
        return "ic_${name}"
    }

    private fun getResourceId(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "mipmap", context.packageName)
    }

    private fun getOutcome(holder: MatchesViewHolder, slot: Int, radiantWin: Boolean): String {
        val winColor = ContextCompat.getColor(holder.itemView.context, R.color.winColor)
        val lossColor = ContextCompat.getColor(holder.itemView.context, R.color.lossColor)

        val outcome = if ((slot < 100) xor radiantWin) "Loss" else "Win"

        if (outcome == "Win") holder.outcome.setTextColor(winColor) else holder.outcome.setTextColor(
            lossColor
        )
        return outcome
    }

    private fun getGamemodeName(gamemode: Int): String {
        val gm: String =
            when (gamemode) {
                0 -> "Unknown"
                1 -> "Ranked"
                2 -> "Captains Mode"
                3 -> "Random Draft"
                4 -> "Single Draft"
                5 -> "All Random"
                6 -> "Intro"
                7 -> "Diretide"
                8 -> "Captains Mode"
                9 -> "Event"
                10 -> "Tutorial"
                11 -> "Mid Only"
                12 -> "Least Played"
                13 -> "Limited Heroes"
                14 -> "Compendium MM"
                15 -> "Custom Mode"
                16 -> "Captains Draft"
                17 -> "Balanced Draft"
                18 -> "Ability Draft"
                19 -> "Event"
                20 -> "All Random DM"
                21 -> "1v1 Mid"
                22 -> "All Pick"
                23 -> "Turbo"
                24 -> "Mutation"
                25 -> "Coaching"
                else -> "Unknown"
            }
        return gm
    }

    private fun getDurationHMS(_seconds: Int): String {
        val hours = _seconds / 3600
        val minutes = (_seconds % 3600) / 60
        val seconds = _seconds % 60
        return if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, seconds)
        else String.format("%02d:%02d", minutes, seconds)
    }

}