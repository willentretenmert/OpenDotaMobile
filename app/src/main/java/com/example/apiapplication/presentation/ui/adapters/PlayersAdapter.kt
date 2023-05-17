package com.example.apiapplication.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.MatchStats
import com.example.apiapplication.data.RecentMatches

class PlayersAdapter(
    private var dataSetHeroes: List<Hero>,
    private var dataSet: List<MatchStats.Player>
) :
    RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {

    class PlayersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val heroIcon: ImageView = itemView.findViewById(R.id.match_player_hero_icon)
        val score: TextView = itemView.findViewById(R.id.match_player_score)
        val netWorth: TextView = itemView.findViewById(R.id.match_player_net)
        val item0: ImageView = itemView.findViewById(R.id.player_item0)
        val item1: ImageView = itemView.findViewById(R.id.player_item1)
        val item2: ImageView = itemView.findViewById(R.id.player_item2)
        val item3: ImageView = itemView.findViewById(R.id.player_item3)
        val item4: ImageView = itemView.findViewById(R.id.player_item4)
        val item5: ImageView = itemView.findViewById(R.id.player_item5)

        init {
            itemView.setOnClickListener { }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match_player, parent, false)
        return PlayersViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        val currentItem = dataSet[position]
        val kda = "${currentItem.kills}/${currentItem.deaths}/${currentItem.assists}"

        val heroName = getHeroNameByPlayerIndex(dataSetHeroes, dataSet, position)
        val itemIds = arrayOf(
            getItemNameByIndex(currentItem.item_0),
            getItemNameByIndex(currentItem.item_1),
            getItemNameByIndex(currentItem.item_2),
            getItemNameByIndex(currentItem.item_3),
            getItemNameByIndex(currentItem.item_4),
            getItemNameByIndex(currentItem.item_5)
        )
        val heroResId = getResourceId(holder.itemView.context, heroName)
        val itemResId = { i: Int -> getResourceId(holder.itemView.context, itemIds[i] )}


        holder.heroIcon.setImageResource(heroResId)
        holder.score.text = kda
        holder.netWorth.text = currentItem.net_worth.toString()
        holder.item0.setImageResource(itemResId(0))
        holder.item1.setImageResource(itemResId(1))
        holder.item2.setImageResource(itemResId(2))
        holder.item3.setImageResource(itemResId(3))
        holder.item4.setImageResource(itemResId(4))
        holder.item5.setImageResource(itemResId(5))
    }

    override fun getItemCount(): Int = dataSet.size

    // getters
    private fun getHeroNameByPlayerIndex(data: List<Hero>, data2: List<MatchStats.Player>, i: Int): String {
        val name = data.firstOrNull { it.id == data2[i].hero_id }?.name ?: ""
        return "ic_${name}"
    }

    private fun getItemNameByIndex(id: Int): String {
        return "ic_item_$id"
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