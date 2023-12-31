package com.boostcamp.planj.data.repository


import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.dto.LoginResponse
import com.boostcamp.planj.data.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun postSignUp(
        userEmail: String,
        userPwd: String,
        userNickname: String
    ): ApiResult<LoginResponse>

    suspend fun postSignIn(
        userEmail: String,
        userPwd: String,
        deviceToken: String
    ): ApiResult<LoginResponse>

    fun getToken(): Flow<String>

    suspend fun saveUser(id: String)

    fun postSignInNaver(accessToken: String, deviceToken: String): Flow<LoginResponse>

    suspend fun getAllAlarmInfo(): List<AlarmInfo>

    suspend fun deleteAllData()

    suspend fun insertAlarmInfo(alarmInfo: AlarmInfo)

    suspend fun deleteAlarmInfo(alarmInfo: AlarmInfo)

    suspend fun deleteAllAlarm()

    suspend fun deleteAlarmInfoUsingScheduleId(scheduleId: String)

    suspend fun saveFirst(isFirst: Boolean)

    fun isFirst(): Flow<Boolean>
}