package com.boostcamp.planj.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Participant(
    val imgUrl: String,
    val nickname: String,
    val isFinished: Boolean
) : Parcelable