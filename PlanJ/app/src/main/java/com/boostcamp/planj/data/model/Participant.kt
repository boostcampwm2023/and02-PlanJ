package com.boostcamp.planj.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Participant(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("email") val email: String,
    @SerializedName("profileUrl") val profileUrl: String?,
    @SerializedName("finished") val isFinished: Boolean,
    @SerializedName("currentUser") val currentUser: Boolean,
    @SerializedName("author") val isAuthor: Boolean
) : Parcelable