package com.boostcamp.planj.ui

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.boostcamp.planj.R

object UpdateWidget {

    fun updateWidget(context: Context){
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, PlanjWidgetProvider::class.java))
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget_list)
    }

}