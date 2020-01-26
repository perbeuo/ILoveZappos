package com.fangpu.ilovezappos

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

// Price alert worker class
class PriceAlertWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Get the input
        val price = getInputData().getString("alert_price")

        Log.i("WorkManager", price)

        return Result.success()
    }

}