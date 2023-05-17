package com.example.apiapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.models.DotaUserRaspberry
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats

class CommentsAdapter(
    private var dataSet: List<DotaUserRaspberry.Comment>
) :
    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.tv_comment_username)
        val comment: TextView = itemView.findViewById(R.id.tv_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val currentItem = dataSet[position]

        holder.username.text = currentItem.author
        holder.comment.text = currentItem.content

    }

    override fun getItemCount() = dataSet.size

    fun updateData(newHeroes: List<DotaUserRaspberry.Comment>) {
        this.dataSet = newHeroes
        notifyDataSetChanged()
    }
}