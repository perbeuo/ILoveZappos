package com.fangpu.ilovezappos

import android.graphics.Color
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fangpu.ilovezappos.network.BitstampPrice
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.collections.ArrayList
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import androidx.recyclerview.widget.RecyclerView
import com.fangpu.ilovezappos.data.Order
import com.fangpu.ilovezappos.network.OrdersBook


@BindingAdapter("welcomeInfo")
fun bindInfo(infoText: TextView, info: List<BitstampPrice>?) {
    infoText.text = info?.get(0)?.price
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: OrdersBook?) {
    val adapter = recyclerView.adapter as OrderBookAdapter
    if (data != null) {
        var orderList = ArrayList<Order>()
        for (order in data.bids){
            orderList.add(Order(order[0], order[1]))
        }
        adapter.submitList(orderList)
    }
}

@BindingAdapter("orderBid")
fun showOrderBid(bidTextView: TextView, order: Order?) {
    bidTextView.text = order?.price.toString()
}

@BindingAdapter("priceChartData")
fun showPriceChart(priceChart: LineChart, info: List<BitstampPrice>?) {
//    priceChart.setViewPortOffsets(0f, 0f, 0f, 0f)
    priceChart.setBackgroundColor(Color.DKGRAY)

    setupChart(priceChart, 0xF7A35C, "Value")
    // no description text
    priceChart.getDescription().setEnabled(false)

    // enable touch gestures
    priceChart.setTouchEnabled(true)

    // enable scaling and dragging
    priceChart.setDragEnabled(true)
    priceChart.setScaleEnabled(true)

    // if disabled, scaling can be done on x- and y-axis separately
    priceChart.setPinchZoom(false)

    priceChart.setDrawGridBackground(false)
    priceChart.setMaxHighlightDistance(300f)

    val x = priceChart.getXAxis()
//    x.setEnabled(false)

    val y = priceChart.getAxisLeft()
    y.setLabelCount(6, false)
    y.setTextColor(Color.WHITE)
    y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
    y.setDrawGridLines(false)
    y.setAxisLineColor(Color.WHITE)

    priceChart.getAxisRight().setEnabled(false)

    priceChart.getLegend().setEnabled(false)

    priceChart.animateXY(2000, 2000)

    setData(priceChart, info)

    // don't forget to refresh the drawing
    priceChart.invalidate()

}

private fun setupChart(chart: LineChart, color: Int, label: String) {
    val bkColor = 0xf3f3f3
    chart.setNoDataText("No Data")
    val l = chart.legend
    l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
    l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
    val entry = LegendEntry()
    entry.label = label
    entry.formColor = color
    l.setCustom(arrayOf(entry))
    l.form = Legend.LegendForm.CIRCLE
    l.formSize = 6f
    l.textColor = bkColor
    val leftAxis = chart.axisLeft
    leftAxis.textColor = bkColor
    val rightAxis = chart.axisRight
    rightAxis.isEnabled = false
}

private fun setData(
    chart: LineChart,
    info: List<BitstampPrice>?
) {

    val values = ArrayList<Entry>()

    if (info != null) {
        val sortedList = info.sortedWith(compareBy { it.date })
        val baseTime = sortedList[0].date.toLong()
        var time = 0f
        var prevTimeDiff = -1L

        for (priceData in sortedList){
            val timeDiff = priceData.date.toLong() - baseTime
            if (timeDiff != prevTimeDiff){
                values.add(Entry(timeDiff.toFloat(), priceData.price.toFloat()))
                prevTimeDiff = timeDiff
                Log.i("PriceData", priceData.date + ": " + timeDiff.toString() + ": " + priceData.price)
            }

//            val cal = Calendar.getInstance(Locale.ENGLISH)
//            cal.setTimeInMillis(priceData.date.toLong() * 1000L)
//            val date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString()
//            Log.i("PriceData", priceData.date + ": " + timeDiff.toString() + ": " + priceData.price)
            time++
        }
    }

    val set1: LineDataSet

    if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
        set1 = chart.getData().getDataSetByIndex(0) as LineDataSet
        set1.values = values
        chart.getData().notifyDataChanged()
        chart.notifyDataSetChanged()
    } else {
        // create a dataset and give it a type
        val set = LineDataSet(values, "Time series")
        set.setDrawFilled(true)// 填充
        set.setDrawCircles(false)
        set.fillAlpha = 40
        set.lineWidth = 1f
        set.circleRadius = 4f
        set.color = 0xff0000
        set.setCircleColor(Color.WHITE)
        set.highLightColor = Color.rgb(244, 117, 117)
        set.valueTextColor = -0xc0c0d

        // create a data object with the data sets
        val data = LineData(set)
        data.setValueTextSize(9f)
        data.setDrawValues(false)

        // set data
        chart.setData(data)
    }
}
