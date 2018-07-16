package com.example.tsvetelinastoyanova.weatherapp.citiesList.visualization

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.Constants
import com.example.tsvetelinastoyanova.weatherapp.R
import java.util.ArrayList

typealias OnItemClickListener = (View, Int) -> Unit

class CitiesAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<CitiesAdapter.MyViewHolder>() {

    val citiesList: MutableList<City> = ArrayList()

    class MyViewHolder internal constructor(view: View, private val onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(view), CityRowView, View.OnClickListener {
        var name: TextView
        var temperature: TextView
        var icon: ImageView
        internal var viewBackground: RelativeLayout
        internal var viewForeground: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            temperature = view.findViewById(R.id.temperature)
            icon = view.findViewById(R.id.icon)
            viewBackground = view.findViewById(R.id.view_background)
            viewForeground = view.findViewById(R.id.view_foreground)
            view.setOnClickListener(this)
        }

        override fun setName(name: String) {
            this.name.text = name
        }

        override fun setTemperature(temperature: Double) {
            this.temperature.text = String.format(Constants.DEGREES_CELSIUS, temperature)
        }

        override fun setIcon(iconId: Int) {
            this.icon.setImageResource(iconId)
        }

        override fun onClick(v: View) {
            onItemClickListener.invoke(v, adapterPosition)
        }
    }

    fun addNewCityToShow(city: City) {
        if (!citiesList.contains(city)) {
            citiesList.add(city)
            notifyDataSetChanged()
        }
    }

    fun getCityNameOnIndex(index: Int): String {
        return citiesList[index].name
    }

    fun getCityOnIndex(index: Int): City {
        return citiesList[index]
    }

    fun removeCity(position: Int) {
        citiesList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreCity(city: City, position: Int) {
        citiesList.add(position, city)
        notifyItemInserted(position)
    }

    fun refreshCity(newCity: City) {
        for (i in 0 until citiesList.size) {
            val c = citiesList[i]
            if (c.name.equals(newCity.name)) {
                citiesList[i] = newCity
                notifyItemChanged(i)
                return
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.city, parent, false)

        return MyViewHolder(itemView, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val city = citiesList[position]
        holder.setName(city.name)
        holder.setTemperature(city.temperature)
        holder.setIcon(city.iconId)
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

}