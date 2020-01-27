package com.fangpu.ilovezappos

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.fangpu.ilovezappos.network.BitstampApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import android.content.ComponentName



// Price alert worker class
class PriceAlertWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private var priceAlertJob = Job()
    private val coroutineScope = CoroutineScope(priceAlertJob + Dispatchers.Main )

    override fun doWork(): Result {

        // Get the input price
        val userPrice = inputData.getString(applicationContext.getString(R.string.saved_alert_price_key))!!

        Log.i("WorkManager", userPrice)

        // Get latest price from Bitstamp
        coroutineScope.launch{
            val getTicketHourDeferred = BitstampApi.retrofitService.getTicketHour()
            try{
                val result = getTicketHourDeferred.await()
                val lastPrice = result.last

                // Current price is below user set price
                if (lastPrice.toFloat() < userPrice.toFloat()){
                    createNotification()
                }
                Log.i("PriceAlert", "User set price: " + userPrice
                        + " Last price: " + lastPrice)
            } catch (e: Exception){
                Log.e("PriceAlert", "No data retrieved")
            }
        }

        return Result.success()
    }

    private fun createNotification() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.component = ComponentName(applicationContext, MainActivity::class.java)
        // Check if the activity exists
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val builder = applicationContext.let { it1 ->
            NotificationCompat.Builder(it1, CHANNEL_ID)
                .setContentTitle("Price Alert")
                .setContentText("Your price is dropped below current market price!")
                .setSmallIcon(R.drawable.notification_icon)
                .setOnlyAlertOnce(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(2398, builder!!.build())
        }
    }

}