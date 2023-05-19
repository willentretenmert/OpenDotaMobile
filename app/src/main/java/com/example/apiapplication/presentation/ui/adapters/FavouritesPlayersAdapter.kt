package com.example.apiapplication.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apiapplication.R
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.data.models.PlayersWinrate
import com.example.apiapplication.databinding.ItemPlayerstatsBinding

class FavouritesPlayersAdapter(
    private var dataHero: List<Hero>,
    private var dataSetProfile: List<PlayersProfile>,
    private var dataSetHeroStats: List<List<PlayersHeroStats>>,
    private var dataSetWinrate: List<PlayersWinrate>
) : RecyclerView.Adapter<FavouritesPlayersAdapter.FavouritesPlayersViewHolder>() {
    inner class FavouritesPlayersViewHolder(val binding: ItemPlayerstatsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val playerAvatar: ImageView = itemView.findViewById(R.id.user_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesPlayersViewHolder {
        val binding =
            ItemPlayerstatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouritesPlayersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouritesPlayersViewHolder, position: Int) {
        val currentProfile = dataSetProfile[position]
        val currentHeroStats = dataSetHeroStats[position]
        val currentWinrate = dataSetWinrate[position]

        val rankId = getResourceId(holder.itemView.context, getPlayersRank(currentProfile))
        with(holder.binding) {
            playerstatsNickname.text = currentProfile.profile?.personaname
            playerstatsRank.setImageResource(rankId)
            Glide.with(holder.playerAvatar.context)
                .load(currentProfile.profile?.avatarmedium)
                .into(holder.playerAvatar)

            profileBriefing.briefingWinrate.text =
                getPlayersWinrate(currentWinrate).toString() + "%"
            profileBriefing.briefingTotalMatches.text = getPlayersTotal(currentWinrate)

            for (i in 0..2) {
                val heroName = getHeroNameByPlayerIndex(dataHero, dataSetHeroStats[position], i)
                val heroResId = getResourceId(holder.itemView.context, heroName)
                when (i) {
                    0 -> {
                        hero1.heroIcon.setImageResource(heroResId)
                        hero1.heroGames.text = currentHeroStats[i].games.toString()
                        hero1.heroWinrate.text =
                            (currentHeroStats[i].win / currentHeroStats[i].games).toString()
                    }

                    1 -> {
                        hero2.heroIcon.setImageResource(heroResId)
                        hero2.heroGames.text = currentHeroStats[i].games.toString()
                        hero2.heroWinrate.text =
                            (currentHeroStats[i].win / currentHeroStats[i].games).toString()
                    }

                    2 -> {
                        hero3.heroIcon.setImageResource(heroResId)
                        hero3.heroGames.text = currentHeroStats[i].games.toString()
                        hero3.heroWinrate.text =
                            (currentHeroStats[i].win / currentHeroStats[i].games).toString()
                    }
                }
            }
        }
    }

    override fun getItemCount() = dataSetProfile.size

    fun updateData(
        newProfile: List<PlayersProfile>,
        newHeroStats: List<List<PlayersHeroStats>>,
        newWinrate: List<PlayersWinrate>
    ) {
        this.dataSetProfile = newProfile
        this.dataSetHeroStats = newHeroStats
        this.dataSetWinrate = newWinrate
        notifyDataSetChanged()
    }

    private fun getHeroNameByPlayerIndex(
        data: List<Hero>,
        data2: List<PlayersHeroStats>,
        i: Int
    ): String {
        val name = data.firstOrNull { it.id == data2[i].hero_id }?.name ?: ""
        return "ic_${name}"
    }

    private fun getPlayersWinrate(data: PlayersWinrate?): Int {
        val wins: Int = data?.win ?: 0
        val total: Int = wins + (data?.lose ?: 0)
        return if (total != 0) (wins.toDouble() / total * 100).toInt() else 0
    }

    private fun getPlayersTotal(data: PlayersWinrate?): String {
        return ((data?.win ?: 0) + (data?.lose ?: 0)).toString()
    }

    private fun getResourceId(context: Context, resName: String): Int {
        return context.resources.getIdentifier(resName, "mipmap", context.packageName)
    }

    private fun getPlayersRank(data: PlayersProfile?): String {
        return "ic_rank${data?.rank_tier}"
    }
}