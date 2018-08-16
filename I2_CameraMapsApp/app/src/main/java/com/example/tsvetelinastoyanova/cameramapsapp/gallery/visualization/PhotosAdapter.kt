package com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization

import android.view.LayoutInflater
import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.example.tsvetelinastoyanova.cameramapsapp.GlideApp
import com.example.tsvetelinastoyanova.cameramapsapp.R

class PhotosAdapter(private val photosList: MutableList<Photo>, private val context: Context,
                    private val WIDTH: Int, private val HEIGHT: Int, private val listener: (Photo) -> Unit)
    : RecyclerView.Adapter<PhotosAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var photo: ImageView = view.findViewById(R.id.photo)

        fun bind(photo: Photo, listener: (Photo) -> Unit) {
            view.setOnClickListener { listener(photo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photoWithDetails = photosList[position]
        holder.bind(photoWithDetails, listener)
        val fileUri: Uri = Uri.fromFile(photoWithDetails.file)

        GlideApp.with(context)
            .asBitmap()
            .placeholder(R.drawable.ic_terrain_black_24dp)
            .load(fileUri)
            .override(WIDTH - 2, HEIGHT - 2)
            .fitCenter()
            .centerCrop()
            .into(holder.photo)
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    fun addNewPhoto(photo: Photo) {
        photosList.add(photo)
    }
}