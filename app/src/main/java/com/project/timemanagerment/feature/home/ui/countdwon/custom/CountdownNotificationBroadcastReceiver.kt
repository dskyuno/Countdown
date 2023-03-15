package com.project.timemanagerment.feature.home.ui.countdwon.custom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.project.timemanagerment.R
import com.project.timemanagerment.base.util.ToastUtils
import com.project.timemanagerment.feature.home.data.repository.CountdownRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CountdownNotificationBroadcastReceiver : BroadcastReceiver() {
    val id = 10000

    @Inject
    lateinit var countdownRepository: CountdownRepository
    override fun onReceive(context: Context?, p1: Intent?) {
        Log.e("ee", "------")
        val localDate = LocalDate.now()   // your current date time
        val startOfDay: LocalDateTime = localDate.atStartOfDay()
        val timestamp = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val todayDate = Date(timestamp)
 /*       CoroutineScope(Dispatchers.IO).launch {
            val list = countdownRepository.findTodayCountdowns(todayDate)
            if (list.isNotEmpty()) {
                var builder = context?.let {
                    NotificationCompat.Builder(it, "countdown channel")
                        .setSmallIcon(R.drawable.ic_app_icon)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(list[0].title + "已经到了")
                        .setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText("Much longer text that cannot fit one line...")
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                }
                CoroutineScope(Dispatchers.IO).launch {
                    with(context?.let { NotificationManagerCompat.from(it) }) {
                        // notificationId is a unique int for each notification that you must define
                        if (builder != null) {
                            this?.notify(id, builder.build())
                        }
                    }
                }
            }

        }*/

    }
}