package com.fangpu.ilovezappos

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.work.*
import com.fangpu.ilovezappos.ui.main.OrderBookFragment
import com.fangpu.ilovezappos.ui.main.PriceHistoryFragment
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.main_activity)

        createWorkManager()
        createNotificationChannel()

        val priceHistoryFragment = PriceHistoryFragment()
        val orderBookFragment = OrderBookFragment()

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        transaction.add(R.id.price_history_container, priceHistoryFragment, "price_history")
        transaction.add(R.id.order_book_container, orderBookFragment, "order_book")

        transaction.commit()
    }

    // Create the work of checking real-time price and compare with user set price
    private fun createWorkManager() {

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null) {
            // Get user set price
            val alertPrice = sharedPref.getFloat(getString(R.string.saved_alert_price_key), Float.MAX_VALUE)
            Log.i("Notification", alertPrice.toString())

            // Create work data using user set price
            val priceData = workDataOf(getString(R.string.saved_alert_price_key) to alertPrice.toString())

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
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                getString(R.string.price_alert_worker_name), ExistingPeriodicWorkPolicy.REPLACE, priceAlertRequest)
        }
    }

    // Create a notification channel.(Can be called for multiple times)
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(getString(R.string.channel_id), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
