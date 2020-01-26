package com.fangpu.ilovezappos.ui.main

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.work.*
import com.fangpu.ilovezappos.MainActivity
import com.fangpu.ilovezappos.R
import com.fangpu.ilovezappos.databinding.MainFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import java.util.concurrent.TimeUnit

const val CHANNEL_ID = "com.fangpu.ilovezappos.notify"

class MainFragment : Fragment() {
//    private lateinit var chart: LineChart
//    private lateinit var notificationManager: NotificationManager

    companion object {
        fun newInstance() = PriceHistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<MainFragmentBinding>(
            inflater, R.layout.main_fragment, container, false)
        binding.setLifecycleOwner(this)

        binding.toPriceButton.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_priceHistoryFragment)
        )

        binding.toOrderButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_orderBookFragment)
        )

        createNotificationChannel()

        binding.testNotificationButton.setOnClickListener {

            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            if (sharedPref != null) {
//                with (sharedPref.edit()) {
//                    putInt(getString(R.string.saved_alert_price_key), 23)
//                    commit()
//                }
                val alertPrice = sharedPref.getInt(getString(R.string.saved_alert_price_key), 0)
                Log.i("Notification", alertPrice.toString())
            }



        }

        return binding.root
    }

    // Create a notification channel.(Can be called for multiple times)
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

//    override fun onPause() {
//        super.onPause()
//        Log.i("Notification", "Paused")
//        val builder = this.context?.let { it1 -> NotificationCompat.Builder(it1, CHANNEL_ID)
//            .setContentTitle("Price Drop")
//            .setContentText("Your price set is dropped below current market price!")
//            .setSmallIcon(R.drawable.notification_icon)
//            .setChannelId(CHANNEL_ID)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//        }
//
//        with(NotificationManagerCompat.from(this.context!!)){
//            notify(102, builder!!.build())
//        }
//    }
}


