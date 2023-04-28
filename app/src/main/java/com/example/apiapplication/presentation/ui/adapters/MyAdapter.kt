package com.example.apiapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.Hero

class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var data: List<Hero>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playerstats, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name?.text = data?.get(position)!!.name

        //name = data[position].name
        //holder.itemView.mode.text = data[position].localized_name
        /*val item = data[position]
        holder.bind(item)*/
    }

    override fun getItemCount() = data!!.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var localized_name: TextView? = null
        fun ViewModel(view: View){
            name = view.findViewById(R.id.name)
            localized_name = view.findViewById(R.id.mode)
        }
    }


}