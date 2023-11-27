package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.LoginResponse
import com.boostcamp.planj.data.model.SignInRequest
import com.boostcamp.planj.data.model.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/api/auth/register")
    suspend fun postSignUp(@Body signUpRequest: SignUpRequest): Response<LoginResponse>

    @POST("/api/auth/login")
    suspend fun postSignIn(@Body signInRequest: SignInRequest): Response<LoginResponse>

}