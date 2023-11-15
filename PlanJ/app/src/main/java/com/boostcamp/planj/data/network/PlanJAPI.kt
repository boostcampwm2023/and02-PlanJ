package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.SignUpRequest
import com.boostcamp.planj.data.model.LoginResponse
import com.boostcamp.planj.data.model.SignInRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface PlanJAPI {

    @POST("/api/auth/register")
    suspend fun postSignUp(@Body signUpRequest: SignUpRequest): LoginResponse

    @POST("/api/auth/login")
    suspend fun postSignIn(@Body signInRequest: SignInRequest): LoginResponse

}