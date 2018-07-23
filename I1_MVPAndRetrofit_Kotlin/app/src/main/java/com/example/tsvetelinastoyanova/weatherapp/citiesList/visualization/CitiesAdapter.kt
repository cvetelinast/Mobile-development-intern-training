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
import kotlinx.android.synthetic.main.city.view.*
import java.util.ArrayList

typealias OnItemClickListener = (View, Int) -> Unit

class CitiesAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<CitiesAdapter.MyViewHolder>() {

    val citiesList: MutableList<City> = ArrayList()

    class MyViewHolder internal constructor(view: View, private val onItemClickListener: OnItemClickListener) :
            RecyclerView.ViewHolder(view), CityRowView, View.OnClickListener {

        private var name: TextView = view.name
        var temperature: TextView = view.temperature
        var icon: ImageView = view.icon
        internal var viewBackground: RelativeLayout = view.view_background
        internal var viewForeground: RelativeLayout = view.view_foreground

        init {
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
        for ((index, city) in citiesList.withIndex()) {
            if (city.name == newCity.name) {
                citiesList[index] = newCity
                notifyItemChanged(index)
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