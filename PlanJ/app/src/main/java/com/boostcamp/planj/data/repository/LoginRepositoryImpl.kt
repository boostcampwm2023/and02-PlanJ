package com.boostcamp.planj.data.repository

import android.util.Log
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
        val userRequest = SignUpRequest(userEmail, userPwd, userNickname)

        // TODO: 응답 코드에 따른 처리 필요
        Log.d("postSignUp요청", userRequest.toString())
        val response = apiService.postSignUp(userRequest)
        Log.d("postSignUp결과코드", response.code().toString())
        Log.d("postSignUp결과메시지", response.message())
        Log.d("postSignUp결과바디", response.body().toString())
        return response.isSuccessful
    }
}