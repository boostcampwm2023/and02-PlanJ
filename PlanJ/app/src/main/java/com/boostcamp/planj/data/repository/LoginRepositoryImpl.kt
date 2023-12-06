package com.boostcamp.planj.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.data.db.AlarmInfoDao
import com.boostcamp.planj.data.model.AlarmInfo
import com.boostcamp.planj.data.model.DateTime
import com.boostcamp.planj.data.model.dto.LoginResponse
import com.boostcamp.planj.data.model.dto.SignInNaverRequest
import com.boostcamp.planj.data.model.dto.SignInRequest
import com.boostcamp.planj.data.model.dto.SignUpRequest
import com.boostcamp.planj.data.network.ApiResult
import com.boostcamp.planj.data.network.LoginApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: LoginApi,
    private val dataStore: DataStore<Preferences>,
    private val alarmInfoDao: AlarmInfoDao,
) : LoginRepository {

    companion object {
        val USER = stringPreferencesKey("User")
        val ALARM_MODE = booleanPreferencesKey("alarm")
    }

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

    override suspend fun saveUser(id: String) {
        dataStore.edit { prefs ->
            prefs[USER] = id
        }
        Log.d("PLANJDEBUG", "dataStore 저장 완료 ${getToken().first()}")
    }


    override fun getToken(): Flow<String> {
        return dataStore.data
            .catch { e ->
                if (e is IOException) {
                    e.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }.map { pref ->
                pref[USER] ?: ""
            }
    }


    override fun postSignInNaver(accessToken: String): Flow<LoginResponse> = flow {
        emit(apiService.postSignInNaver(SignInNaverRequest(accessToken)))
    }

    override suspend fun updateAlarmInfo(curTimeMillis: Long) {
        val alarmList = alarmInfoDao.getAll()
        val calendar = Calendar.getInstance()
        alarmList.forEach { alarmInfo ->
            calendar.timeInMillis = alarmInfo.endTime.toMilliseconds()
            calendar.add(Calendar.MINUTE, -alarmInfo.alarm.alarmTime - alarmInfo.estimatedTime)

            // 알림 시간 < 현재 시간 -> 알람 삭제 or 업데이트
            if (calendar.timeInMillis < curTimeMillis) {
                // 일회성 -> 알림 DB에서 삭제
                // 반복 -> DB 업데이트
                if (alarmInfo.repetition == null) {
                    alarmInfoDao.deleteAlarmInfo(alarmInfo)
                } else {
                    val oneDayMillis = 24 * 60 * 60 * 1000L
                    val interval = if (alarmInfo.repetition.cycleType == "DAILY") {
                        alarmInfo.repetition.cycleCount
                    } else {
                        alarmInfo.repetition.cycleCount * 7
                    }
                    while (calendar.timeInMillis < curTimeMillis) {
                        calendar.timeInMillis += (interval * oneDayMillis)
                    }
                    calendar.add(
                        Calendar.MINUTE,
                        alarmInfo.alarm.alarmTime + alarmInfo.estimatedTime
                    )
                    val endTime = DateTime(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    )
                    alarmInfoDao.updateAlarmInfo(
                        alarmInfo.copy(endTime = endTime)
                    )
                }
            }
        }
    }

    override suspend fun getAllAlarmInfo(): List<AlarmInfo> {
        return alarmInfoDao.getAll()
    }

    override suspend fun deleteAllData() {
        alarmInfoDao.deleteAllAlarmInfo()
    }

    override suspend fun insertAlarmInfo(alarmInfo: AlarmInfo) {
        alarmInfoDao.insertAlarmInfo(alarmInfo)
    }

    override suspend fun deleteAlarmInfo(alarmInfo: AlarmInfo) {
        alarmInfoDao.deleteAlarmInfo(alarmInfo)
    }

    override suspend fun deleteAlarmInfoUsingScheduleId(scheduleId: String) {
        alarmInfoDao.deleteAlarmInfoUsingScheduleId(scheduleId)
    }

    override suspend fun saveAlarmMode(mode: Boolean) {
        dataStore.edit { prefs ->
            prefs[ALARM_MODE] = mode
        }
    }

    override fun getAlarmMode(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }

            }
            .map { prefs ->
                prefs[ALARM_MODE] ?: true
            }
    }
}