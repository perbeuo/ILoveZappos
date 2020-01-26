package com.fangpu.ilovezappos

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.*
import com.fangpu.ilovezappos.ui.main.PriceHistoryFragment
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, PriceHistoryFragment.newInstance())
//                    .commitNow()
//        }
        createWorkManager()
    }
    private fun createWorkManager() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null) {
            // Get user set price
            val alertPrice = sharedPref.getInt(getString(R.string.saved_alert_price_key), -1)
            Log.i("Notification", alertPrice.toString())

            // Create work data using user set price
            val priceData = workDataOf(getString(R.string.saved_alert_price_key) to alertPrice.toString())

            // Create period job, repeat every 1 hour
            val priceAlertRequest =
                PeriodicWorkRequestBuilder<PriceAlertWorker>(1, TimeUnit.MINUTES)
                    .setInputData(priceData)
                    .build()

            WorkManager.getInstance(this).enqueue(priceAlertRequest)
        }
    }
}
