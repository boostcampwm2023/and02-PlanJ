package com.boostcamp.planj.data.repository

import android.util.Log
import com.boostcamp.planj.data.model.SignInRequest
import com.boostcamp.planj.data.model.SignUpRequest
import com.boostcamp.planj.data.network.PlanJAPI
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: PlanJAPI
) : LoginRepository {

    override suspend fun postSignUp(
        userEmail: String,
        userPwd: String,
        userNickname: String
    ): Boolean {
        val userSignUpRequest = SignUpRequest(userEmail, userPwd, userNickname)

        // TODO: 응답 코드에 따른 처리 필요
        val response = apiService.postSignUp(userSignUpRequest)
        return response.statusCode == "201"
    }

    override suspend fun postSignIn(userEmail: String, userPwd: String): Boolean {
        val userSignInRequest = SignInRequest(userEmail, userPwd)

        // TODO: 응답 코드에 따른 처리 필요
        val response = apiService.postSignIn(userSignInRequest)
        Log.d("Login-SignIn 코드", response.statusCode)
        Log.d("Login-SignIn 메시지", response.message)
        return response.statusCode=="200"
    }
}