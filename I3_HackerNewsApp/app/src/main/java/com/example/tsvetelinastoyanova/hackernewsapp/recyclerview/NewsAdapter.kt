package com.example.tsvetelinastoyanova.hackernewsapp.recyclerview

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.hackernewsapp.R
import com.example.tsvetelinastoyanova.hackernewsapp.common.Utils.convertStoryToNew
import com.example.tsvetelinastoyanova.hackernewsapp.data.NetworkState
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

class NewsAdapter : PagedListAdapter<Story, NewsAdapter.MyViewHolder>(StoriesComparator) {

    private var networkState: NetworkState? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val score: TextView = view.findViewById(R.id.score)
        val datetime: TextView = view.findViewById(R.id.datetime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_new, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            val new = convertStoryToNew(it)
            holder.title.text = new.title
            holder.score.text = new.score
            holder.datetime.text = new.datetime
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null  && networkState != NetworkState.LOADED
    }

    companion object {
        val StoriesComparator = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldStory: Story, newStory: Story): Boolean {
                return oldStory.id == newStory.id
            }

            override fun areContentsTheSame(oldStory: Story, newStory: Story): Boolean {
                return oldStory == newStory
            }
        }
    }
}