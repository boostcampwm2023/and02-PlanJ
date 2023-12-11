package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.dto.GetSchedulesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    @GET("/api/schedule/daily")
    suspend fun getDailySchedule(@Query("date") date: String): GetSchedulesResponse
}