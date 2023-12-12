package com.boostcamp.planj.ui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.boostcamp.planj.R
import com.boostcamp.planj.ui.main.MainActivity
import com.boostcamp.planj.ui.schedule.ScheduleActivity
import java.time.LocalDate

class PlanjWidgetProvider : AppWidgetProvider() {

    companion object {
        const val TOAST_ACTION = "com.boostcamp.planj.ui.PlanjWidgetProvider.TOAST_ACTION"
        const val EXTRA_ITEM = "com.boostcamp.planj.ui.PlanjWidgetProvider.EXTRA_ITEM"
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == TOAST_ACTION) {
            val scheduleId = intent.getStringExtra(EXTRA_ITEM)
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("scheduleId", scheduleId)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            Log.d("PLANJDEBUG1234", "refresh Update")
            val ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(ComponentName(context, PlanjWidgetProvider::class.java))
            val myWidget = PlanjWidgetProvider()
            myWidget.onUpdate(context, AppWidgetManager.getInstance(context), ids)
        }

    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget_list)

        appWidgetIds.forEach { appWidgetId ->
            val serviceIntent = Intent(context, PlanjViewService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            RemoteViews(context.packageName, R.layout.widget_layout).apply {
                Log.d("PLANJDEBUG1234", "onUpdate")
                val intentAction = Intent(context, PlanjWidgetProvider::class.java)
                intentAction.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(ComponentName(context, PlanjWidgetProvider::class.java))
                intentAction.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

                val date = LocalDate.now()
                setTextViewText(
                    R.id.tv_widget_today,
                    String.format("%04d-%02d-%02d", date.year, date.monthValue, date.dayOfMonth)
                )

                setRemoteAdapter(R.id.lv_widget_list, serviceIntent)

                val toastPendingIntent: PendingIntent = Intent(
                    context,
                    PlanjWidgetProvider::class.java
                ).run {
                    action = TOAST_ACTION
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))

                    PendingIntent.getBroadcast(
                        context,
                        0,
                        this,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                    )
                }
                setPendingIntentTemplate(R.id.lv_widget_list, toastPendingIntent)


                setOnClickPendingIntent(
                    R.id.iv_widget_main,
                    Intent(context, MainActivity::class.java).let {
                        PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
                    })

                setOnClickPendingIntent(
                    R.id.iv_widget_refresh,
                    PendingIntent.getBroadcast(
                        context,
                        0,
                        intentAction,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )

            }.also { views ->
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }

        }
    }
}