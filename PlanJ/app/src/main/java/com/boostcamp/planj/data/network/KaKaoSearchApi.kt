package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.SearchMap
import retrofit2.http.GET
import retrofit2.http.Query

interface KaKaoSearchApi {

    @GET("local/search/keyword.json")
    suspend fun searchMap(
        @Query("query") query : String,
    ) : SearchMap


}