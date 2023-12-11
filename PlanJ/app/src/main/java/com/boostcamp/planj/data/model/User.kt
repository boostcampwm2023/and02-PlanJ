package com.boostcamp.planj.data.model

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("profileUrl") val profileUrl: String? = null,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String
) : Parcelable


