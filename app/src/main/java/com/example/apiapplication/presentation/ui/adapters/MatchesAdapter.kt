package com.example.apiapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.Hero

class MatchesAdapter : RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>() {

    class MatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        private var localizedName: TextView? = null
        fun viewModel(view: View){
            name = view.findViewById(R.id.hero)
            localizedName = view.findViewById(R.id.mode)
        }
    }

    private var data: List<Hero>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playerstats, parent,false)
        return MatchesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.name?.text = data?.get(position)!!.name

        //name = data[position].name
        //holder.itemView.mode.text = data[position].localized_name
        /*val item = data[position]
        holder.bind(item)*/
    }

    override fun getItemCount() = data!!.size

}