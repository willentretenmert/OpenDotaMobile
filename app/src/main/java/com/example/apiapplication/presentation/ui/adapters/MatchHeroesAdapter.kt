package com.example.apiapplication.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats

class MatchHeroesAdapter(
    private var dataSetHeroes: List<Hero>,
    private var dataSet: List<MatchStats.Player>
) :
    RecyclerView.Adapter<MatchHeroesAdapter.MatchesHeroesViewHolder>() {

    class MatchesHeroesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val heroIcon: ImageView

        init {
            itemView.setOnClickListener { }
            heroIcon = itemView.findViewById(R.id.match_briefing_hero_icon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesHeroesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_match_briefing_hero_icon, parent, false)
        return MatchesHeroesViewHolder(view)
    }


    override fun onBindViewHolder(holder: MatchesHeroesViewHolder, position: Int) {
        val heroName = getHeroNameByRecentMatchesIndex(dataSetHeroes, dataSet, position)
        val resId = getResourceId(holder.itemView.context, heroName)

        holder.heroIcon.setImageResource(resId)
    }

    override fun getItemCount() = dataSet.size

    private fun getHeroNameByRecentMatchesIndex(
        data: List<Hero>,
        data2: List<MatchStats.Player>,
        i: Int
    ): String {
        val name = data.firstOrNull { it.id == data2[i].hero_id }?.name ?: ""
        return "ic_${name}"
    }

    private fun getResourceId(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "mipmap", context.packageName)
    }

    fun updateData(newHeroes: List<Hero>, newPlayers: List<MatchStats.Player>) {
        this.dataSetHeroes = newHeroes
        this.dataSet = newPlayers
        notifyDataSetChanged()
    }
}
