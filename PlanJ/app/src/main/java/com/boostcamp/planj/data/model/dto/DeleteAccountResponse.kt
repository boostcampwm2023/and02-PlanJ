package com.boostcamp.planj.data.model.dto

import com.google.gson.annotations.SerializedName

data class DeleteAccountResponse(
    @SerializedName("message") val message : String,
    @SerializedName("statusCode") val statusCode : String,

)