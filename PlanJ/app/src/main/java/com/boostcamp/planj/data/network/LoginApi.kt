package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.dto.SignInNaverRequest
import com.boostcamp.planj.data.model.dto.LoginResponse
import com.boostcamp.planj.data.model.dto.SignInRequest
import com.boostcamp.planj.data.model.dto.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/api/auth/register")
    suspend fun postSignUp(@Body signUpRequest: SignUpRequest): Response<LoginResponse>

    @POST("/api/auth/login")
    suspend fun postSignIn(@Body signInRequest: SignInRequest): Response<LoginResponse>

    @POST("/api/auth/naver")
    suspend fun postSignInNaver(@Body accessToken : SignInNaverRequest) : LoginResponse

}