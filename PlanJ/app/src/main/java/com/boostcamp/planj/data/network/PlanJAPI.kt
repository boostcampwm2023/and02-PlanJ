package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PlanJAPI {

    @POST("/api/auth/register")
    suspend fun postSignUp(@Body signUpRequest: SignUpRequest): Response<String>
}