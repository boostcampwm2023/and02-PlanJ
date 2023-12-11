package com.boostcamp.planj.ui

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.ScheduleInfo
import java.time.LocalDate


class PlanjRemoteViewsFactory(
    private val context: Context,
    private val getData: (MutableList<ScheduleInfo>) -> Unit
) : RemoteViewsService.RemoteViewsFactory {

    private var data = mutableListOf<ScheduleInfo>()

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            val schedule = data[position]
            val current = LocalDate.now()
            val isCurrent = schedule.endAt.split("T").first() == String.format(
                "%04d-%02d-%02d", current.year, current.monthValue, current.dayOfMonth
            )

            val time = schedule.endAt.split("T").last().split(":")
            val endTime = if (isCurrent) {
                "오늘 ${time[0]}:${time[1]}까지"
            } else {
                "${schedule.endAt.split("T").first().replace("-", "/")} ${time[0]}:${time[1]}까지"
            }

            val intent = Intent(context, PlanjWidgetProvider::class.java)
            intent.putExtra(PlanjWidgetProvider.EXTRA_ITEM, schedule.scheduleUuid)
            setOnClickFillInIntent(R.id.layout_widget_item_root, intent)
            setTextViewText(R.id.tv_widget_title, schedule.title)
            setTextViewText(R.id.tv_widget_time, endTime)
        }

    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreate() {
        getData(data)
        Thread.sleep(100)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewTypeCount(): Int = 1

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun onDataSetChanged() {
        getData(data)
        Thread.sleep(100)
    }

    override fun getCount(): Int = data.size

    override fun onDestroy() {

    }

}