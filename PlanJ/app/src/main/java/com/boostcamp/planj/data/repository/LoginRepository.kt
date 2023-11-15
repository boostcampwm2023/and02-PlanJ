package com.boostcamp.planj.data.repository

interface LoginRepository {
    suspend fun postSignUp(userEmail: String, userPwd: String, userNickname: String): Boolean
}