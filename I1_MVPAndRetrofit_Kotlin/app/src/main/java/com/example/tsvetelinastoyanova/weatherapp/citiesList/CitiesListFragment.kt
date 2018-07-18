package com.example.tsvetelinastoyanova.weatherapp.citiesList

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.R
import com.example.tsvetelinastoyanova.weatherapp.citiesList.visualization.CitiesAdapter
import com.example.tsvetelinastoyanova.weatherapp.citiesList.visualization.RecyclerItemTouchHelper
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import kotlinx.android.synthetic.main.fragment_cities_list.*
import kotlinx.android.synthetic.main.fragment_cities_list.view.*


class CitiesListFragment : Fragment(), CitiesListContract.View, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private var citiesAdapter: CitiesAdapter? = null
    private var presenter: CitiesListContract.Presenter? = null
    private var cityNameContainer: TextInputLayout? = null

    private var isFirstTimeLoading = true

    /*** interface  */

    private var onClickCityDelegate: OnClickCityDelegate? = null

    interface OnClickCityDelegate {
        fun onClickCity(currentWeatherObject: CurrentWeatherObject)
    }

    /*** Methods from Fragment  */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            onClickCityDelegate = context as OnClickCityDelegate?
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement OnHeadlineSelectedListener")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cities_list, container, false)
        addClickListenerToAddCityButton(view.addCityButton)
        setTextContainer(view.newCityWrapper)
        createRecyclerView(view.recyclerView)
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter?.let {
            if (isFirstTimeLoading) {
                isFirstTimeLoading = false
                it.start()
            }
        }
    }

    /*** Methods from CitiesListContract.View  */

    override fun setWeatherObjectWhenClicked(cityName: String) {
        val weatherObject = presenter?.getWeatherObjectOnClick(cityName)
        weatherObject?.let {
            onClickCityDelegate?.onClickCity(weatherObject)
        }
    }

    override fun setPresenter(presenter: CitiesListContract.Presenter) {
        presenter.let {
            this.presenter = presenter
        }
    }

    override fun showNewCityAdded(newCity: City) {
        citiesAdapter?.addNewCityToShow(newCity)
        Toast.makeText(context, R.string.added_city, Toast.LENGTH_SHORT).show()
        cityNameContainer?.editText?.setText("")
    }

    override fun showErrorAddingAddedCity() {
        Toast.makeText(context, R.string.not_added_city, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorLoadingCities() {
        Toast.makeText(context, R.string.problem_loading_cities, Toast.LENGTH_SHORT).show()
    }

    override fun showCityDeleted() {
        Toast.makeText(context, R.string.city_deleted_success, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorDeleteCity() {
        Toast.makeText(context, R.string.problem_deleting_cities, Toast.LENGTH_SHORT).show()
    }

    override fun showCityLoaded(city: City) {
        citiesAdapter?.addNewCityToShow(city)
    }

    override fun showCityUpdated(newCity: City) {
        citiesAdapter?.refreshCity(newCity)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is CitiesAdapter.MyViewHolder) {
            val currentCityName = citiesAdapter?.getCityNameOnIndex(position)

            // backup of removed item for undo purpose
            val deletedItem = citiesAdapter?.getCityOnIndex(position)
            val deletedIndex = viewHolder.getAdapterPosition()

            citiesAdapter?.removeCity(viewHolder.getAdapterPosition())

            showSnackbarWithUndo(currentCityName, deletedItem, deletedIndex)
        }
    }

    override fun getDisplayedCities(): MutableList<City>? {
        return citiesAdapter?.citiesList
    }

    private fun showSnackbarWithUndo(name: String?, deletedItem: City?, deletedIndex: Int) {
        val snackbar = Snackbar
                .make(coordinatorLayout, resources.getString(R.string.city_removed_message, name), Snackbar.LENGTH_LONG)

        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                if (event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_TIMEOUT) {
                    deletedItem?.let {
                        presenter?.deleteCity(it)
                    }
                }
            }
        }).setAction(R.string.undo, { view ->
            // undo is selected, restore the deleted item
            deletedItem?.let {
                citiesAdapter?.restoreCity(deletedItem, deletedIndex)
            }
        })

        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.show()
    }

    private fun addClickListenerToAddCityButton(addCityButton: Button) {
        addCityButton.setOnClickListener { _ -> presenter?.addNewCity(cityNameContainer?.editText?.text.toString()) }
    }

    private fun setTextContainer(t: TextInputLayout) {
        cityNameContainer = t
    }

    private fun createRecyclerView(recyclerView: RecyclerView) {
        val onItemClickListener = lambda@{ _: View, position: Int ->
            citiesAdapter?.let {
                setWeatherObjectWhenClicked(it.getCityNameOnIndex(position))
            }

            return@lambda
        }

        citiesAdapter = CitiesAdapter(onItemClickListener)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = citiesAdapter

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }
}