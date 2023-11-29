package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.naver.NaverResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverApi {

    @GET("/map-direction/v1/driving")
    suspend fun getNaverRoute(
        @Query("start") start : String,
        @Query("goal") goal : String,
        @Query("option") option : String,
    ) : NaverResponse
}