package com.boostcamp.planj.ui.adapter

import com.boostcamp.planj.data.model.Schedule

fun interface ScheduleDoneListener {
    fun onClick(schedule : Schedule)
}