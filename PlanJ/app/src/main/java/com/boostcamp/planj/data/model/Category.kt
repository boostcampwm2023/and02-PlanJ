package com.boostcamp.planj.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Category(
    @SerializedName("categoryUuid") val categoryUuid: String,
    @SerializedName("categoryName") val categoryName: String
) : Parcelable
