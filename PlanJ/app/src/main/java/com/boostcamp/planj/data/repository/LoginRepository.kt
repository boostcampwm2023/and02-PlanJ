package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.LoginResponse
import com.boostcamp.planj.data.model.dto.DeleteAccountResponse
import com.boostcamp.planj.data.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun postSignUp(
        userEmail: String,
        userPwd: String,
        userNickname: String
    ): ApiResult<LoginResponse>

    suspend fun postSignIn(userEmail: String, userPwd: String): ApiResult<LoginResponse>

    fun getUser() : Flow<String>

    suspend fun saveUser(id : String)

}