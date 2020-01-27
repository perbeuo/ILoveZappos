package com.fangpu.ilovezappos.ui.main

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.fangpu.ilovezappos.OrderBookAdapter
import com.fangpu.ilovezappos.PriceAlertWorker
import com.fangpu.ilovezappos.R
import com.fangpu.ilovezappos.databinding.OrderBookFragmentBinding
import com.fangpu.ilovezappos.databinding.PriceHistoryFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import java.lang.Double.parseDouble
import java.util.concurrent.TimeUnit

class OrderBookFragment : Fragment() {

    private val viewModel: OrderBookViewModel by lazy {
        ViewModelProviders.of(this).get(OrderBookViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = OrderBookFragmentBinding.inflate(inflater)
        val adapter = OrderBookAdapter()

        binding.bidDataRecycler.adapter = adapter
        binding.lifecycleOwner = this
        binding.orderData = viewModel

        binding.swipeContainer.setOnRefreshListener {
            viewModel.getInternetData()
            binding.swipeContainer.isRefreshing = false
        }

        binding.setAlertPriceButton.setOnClickListener {
            setUserPriceAlert()
        }
        return binding.root
    }

    private fun setUserPriceAlert() {
        val input = EditText(context)
        input.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER

        activity?.let {
            val builder = AlertDialog.Builder(it, R.style.Theme_MaterialComponents_Light_Dialog)
            builder.apply {
                setPositiveButton(
                    R.string.set
                ) { _, _ ->
                    val userPrice = input.text.toString()
                    var isValidInput = true
                    Log.i("PriceAlert", "Get user input of $userPrice")

                    // Check if the input is a valid number
                    try {
                        parseDouble(userPrice)
                    } catch (e: NumberFormatException) {
                        isValidInput = false
                    }
                    if (isValidInput) {
                        saveUserPriceData(userPrice)
                        createWorkManager(userPrice)
                    }
                }
                setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.cancel()
                }
                setMessage(R.string.price_alert_dialog_message)
                setTitle(R.string.price_alert_dialog_title)
                setView(input)
            }

            // Create the AlertDialog
            builder.create()
            builder.show()
        }
    }

    private fun createWorkManager(price: String) {
        // Create work data using user set price
        val priceData = workDataOf(getString(R.string.saved_alert_price_key) to price)

        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        // Create period job, repeat every 1 hour
        val priceAlertRequest =
            PeriodicWorkRequestBuilder<PriceAlertWorker>(1, TimeUnit.HOURS)
                .setInputData(priceData)
                .addTag(getString(R.string.price_alert_tag))
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(activity!!).enqueueUniquePeriodicWork(
            getString(R.string.price_alert_worker_name), ExistingPeriodicWorkPolicy.REPLACE, priceAlertRequest)
    }

    private fun saveUserPriceData(price: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null) {
            // Save user set price data
            with(sharedPref.edit()) {
                putFloat(getString(R.string.saved_alert_price_key), price.toFloat())
                commit()
            }
        }
    }

}
