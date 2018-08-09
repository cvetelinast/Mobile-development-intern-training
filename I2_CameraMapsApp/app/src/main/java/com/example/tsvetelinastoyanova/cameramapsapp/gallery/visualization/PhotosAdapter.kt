package com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization

import android.view.LayoutInflater
import android.content.Context
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.signature.ObjectKey
import com.example.tsvetelinastoyanova.cameramapsapp.GlideApp
import com.example.tsvetelinastoyanova.cameramapsapp.R
import java.io.File


class PhotosAdapter(private val photosList: MutableList<Photo>, private val context: Context) : RecyclerView.Adapter<PhotosAdapter.MyViewHolder>() {
    private val IMAGE_WIDTH_PIXELS = 500
    private val IMAGE_HEIGHT_PIXELS = 500

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photo: ImageView = view.findViewById(R.id.photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photoWithDetails = photosList[position]
        val file: File = photoWithDetails.file

        GlideApp.with(context)
            .load(file)
            //  .signature(ObjectKey(file.name + file.lastModified()))
            .override(IMAGE_WIDTH_PIXELS, IMAGE_HEIGHT_PIXELS)
            .into(holder.photo)
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    fun addNewPhoto(photo: Photo) {
        photosList.add(photo)
        notifyDataSetChanged()
        // notifyItemChanged(photosList.size)
    }
}