package com.boostcamp.planj.data.repository

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
        val response = apiService.postSignUp(userRequest)
        return response.isSuccessful
    }
}