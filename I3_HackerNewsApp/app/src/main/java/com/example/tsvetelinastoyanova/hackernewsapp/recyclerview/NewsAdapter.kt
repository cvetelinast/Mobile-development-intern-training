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
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

class NewsAdapter(private val listener: (New) -> Unit) : PagedListAdapter<Story, NewsAdapter.MyViewHolder>(StoriesComparator) {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val score: TextView = view.findViewById(R.id.score)
        val datetime: TextView = view.findViewById(R.id.datetime)

        fun bind(new: New, listener: (New) -> Unit) {
            view.setOnClickListener { listener(new) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_new, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            val new = convertStoryToNew(it)
            holder.bind(new, listener)
            holder.title.text = new.title
            holder.score.text = new.score
            holder.datetime.text = new.datetime
        }
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