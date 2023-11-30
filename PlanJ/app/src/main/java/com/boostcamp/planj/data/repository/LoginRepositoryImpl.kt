package com.boostcamp.planj.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.planj.data.model.LoginResponse
import com.boostcamp.planj.data.model.dto.SignInRequest
import com.boostcamp.planj.data.model.dto.SignUpRequest
import com.boostcamp.planj.data.network.ApiResult
import com.boostcamp.planj.data.network.LoginApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: LoginApi,
    private val dataStore: DataStore<Preferences>
) : LoginRepository {

    companion object {
        val USER = stringPreferencesKey("User")
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
        Log.d("PLANJDEBUG", "dataStore 저장 완료 ${getUser().first()}")
    }


    override fun getUser(): Flow<String> {
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

}