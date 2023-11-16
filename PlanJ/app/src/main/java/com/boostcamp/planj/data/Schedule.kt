package com.boostcamp.planj.data

import com.boostcamp.planj.data.model.User

data class Schedule(
    val scheduleId : String,
    val title : String,
    val memo : String?,
    val startTime : String?,
    val endTime : String,
    val categoryTitle : String,
    val members : List<User>,
    val doneMembers : List<User>?,
    val location : String?,
    val finished : Boolean,
    val failed : Boolean
)