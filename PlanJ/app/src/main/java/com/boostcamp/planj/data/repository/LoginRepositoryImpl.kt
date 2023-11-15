package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.LoginResponse
import com.boostcamp.planj.data.model.SignInRequest
import com.boostcamp.planj.data.model.SignUpRequest
import com.boostcamp.planj.data.network.ApiResult
import com.boostcamp.planj.data.network.PlanJAPI
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: PlanJAPI
) : LoginRepository {

    override suspend fun postSignUp(
        userEmail: String,
        userPwd: String,
        userNickname: String
    ): ApiResult<LoginResponse> {
        val response = apiService.postSignUp(SignUpRequest(userEmail, userPwd, userNickname))
        if (response.isSuccessful) {
            response.body()?.let { loginResponse ->
                return ApiResult.Success(loginResponse)
            }
        }
        return ApiResult.Error(response.code())
    }

    override suspend fun postSignIn(userEmail: String, userPwd: String): ApiResult<LoginResponse> {
        val response = apiService.postSignIn(SignInRequest(userEmail, userPwd))
        if (response.isSuccessful) {
            response.body()?.let { loginResponse ->
                return ApiResult.Success(loginResponse)
            }
        }
        return ApiResult.Error(response.code())
    }
}