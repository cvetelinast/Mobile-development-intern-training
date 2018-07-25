package com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tsvetelinastoyanova.weatherapp.R
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import com.example.tsvetelinastoyanova.weatherapp.util.convertTemperatureToCelsius
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.*

class ForecastFragment : Fragment(), ForecastContract.View {

    private lateinit var presenter: ForecastContract.Presenter
    private var chart: CombinedChart? = null
    private val NUMBER_OF_VALUES_ON_X_COORD = 12

    /*** interface ***/

    private var onLoadedForecastFragmentDelegate: OnLoadedForecastFragmentDelegate? = null

    interface OnLoadedForecastFragmentDelegate {
        fun onLoadedForecastFragment()
    }

    fun setForecastDelegate(delegate: OnLoadedForecastFragmentDelegate) {
        Log.d("tag", "Setting forecastDelegate")
        onLoadedForecastFragmentDelegate = delegate
    }

    /*** Methods from Fragment ***/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_forecast_child, container, false)

        this.chart = view.findViewById(R.id.combined_chart)
        return view
    }

    override fun onStart() {
        ////////////////////////////////////////////////////////////////////
        super.onStart()
        Utils.checkNotNull(onLoadedForecastFragmentDelegate) // tyk vnimatelno
        onLoadedForecastFragmentDelegate?.onLoadedForecastFragment()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ForecastFragment()
    }

    override fun setPresenter(presenter: ForecastContract.Presenter) {
        Utils.checkNotNull(presenter)
        this.presenter = presenter
    }

    private fun generateLineData(temperatures: List<Double>): LineData {
        val lineData = LineData()
        val entries = ArrayList<Entry>()
        for (index in 0 until NUMBER_OF_VALUES_ON_X_COORD) {
            entries.add(Entry(index.toFloat(), temperatures[index].toFloat()))
        }
        val set = LineDataSet(entries, resources.getString(R.string.temperature))
        lineDataSetSettings(set)
        lineData.addDataSet(set)

        return lineData
    }

    private fun lineDataSetSettings(set: LineDataSet) {
        set.color = Color.rgb(240, 238, 70)
        set.lineWidth = 2.5f
        set.setCircleColor(Color.rgb(240, 238, 70))
        set.circleRadius = 5f
        set.fillColor = Color.rgb(240, 238, 70)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 14f
        set.valueTextColor = Color.BLACK
        set.axisDependency = YAxis.AxisDependency.LEFT
    }

    private fun generateBarData(windSpeedValues: List<Double>): BarData {
        val entries = ArrayList<BarEntry>()
        for (index in 0 until NUMBER_OF_VALUES_ON_X_COORD) {
            entries.add(BarEntry(index.toFloat(), windSpeedValues[index].toFloat()))
        }
        val set = BarDataSet(entries, resources.getString(R.string.speed_of_wind))
        barDataSetSettings(set)

        val barWidth = 0.6f
        val d = BarData(set)
        d.barWidth = barWidth
        return d
    }

    private fun barDataSetSettings(set: BarDataSet) {
        set.color = Color.rgb(140, 234, 255)
        set.valueTextColor = Color.BLACK
        set.valueTextSize = 14f
        set.axisDependency = YAxis.AxisDependency.LEFT
    }

    override fun showForecastForCity(forecastObject: ForecastObject) {
        val temperatures = forecastObject.list
            .map { l ->
                l.main.temp.convertTemperatureToCelsius()
                // Utils.(l.main.temp)
            }.take(NUMBER_OF_VALUES_ON_X_COORD)
        val windSpeedValues = forecastObject.list
            .map { l -> l.wind.speed }
            .take(NUMBER_OF_VALUES_ON_X_COORD)
        val times = forecastObject.list
            .map { l ->
                val text = l.dtTxt;text
                .split(" ")[1].removeSuffix(":00")
            }
            .take(NUMBER_OF_VALUES_ON_X_COORD)

        drawChart(windSpeedValues, temperatures, times)
    }

    private fun drawChart(windSpeedValues: List<Double>, temperatures: List<Double>, times: List<String>) {
        val chart = this.chart
        chart?.let {
            doChartSettings(chart)
            doLegendSettings(chart)

            val minWindSpeed: Double = windSpeedValues.min() ?: 0.0
            val minTemperature: Double = temperatures.min() ?: 0.0
            val min = if (minWindSpeed < minTemperature) minWindSpeed else minTemperature

            val xAxis = axisFormatting(chart, min, times)
            val data = CombinedData()

            data.setData(generateLineData(temperatures))
            data.setData(generateBarData(windSpeedValues))

            xAxis.axisMaximum = data.xMax + 0.25f

            chart.data = data
            chart.invalidate()
        }
    }

    private fun axisFormatting(chart: CombinedChart, min: Double, times: List<String>): XAxis {
        val rightAxis = chart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = min.toFloat()

        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = min.toFloat()

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
        xAxis.axisMinimum = -0.2f
        xAxis.granularity = 1f

        xAxis.valueFormatter = IAxisValueFormatter { value, _ -> times[value.toInt()] }
        return xAxis
    }

    private fun doLegendSettings(chart: CombinedChart) {
        val l = chart.legend
        l.isWordWrapEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
    }

    private fun doChartSettings(chart: CombinedChart) {
        chart.description.isEnabled = false
        chart.setBackgroundColor(Color.WHITE)
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        chart.isHighlightFullBarEnabled = false
        chart.animateY(1500)

        // draw bars behind lines
        chart.drawOrder = arrayOf(CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE)
    }
}