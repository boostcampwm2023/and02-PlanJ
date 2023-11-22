package com.boostcamp.planj.ui.main

import com.boostcamp.planj.data.model.Schedule

fun interface SwipeListener {
    fun swipe(schedule: Schedule)
}