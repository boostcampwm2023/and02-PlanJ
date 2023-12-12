package com.boostcamp.planj.ui.main

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.repository.LoginRepository
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private var nickname: String = ""

    private val _userInfo = MutableStateFlow<User?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _userImg = MutableStateFlow<String?>(null)
    val userImg = _userImg.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private val _showToast = MutableSharedFlow<String>()
    val showToast = _showToast.asSharedFlow()

    private val _totalSchedules = MutableStateFlow<List<Schedule>>(emptyList())
    val totalSchedules = _totalSchedules.asStateFlow()

    fun setMode() {
        _isEditMode.value = false
    }

    fun initUser() {
        viewModelScope.launch {
            mainRepository.getMyInfo()
                .catch {
                    Log.d("PLANJDEBUG", "initUser error ${it.message}")
                }
                .collectLatest { user ->
                    _userInfo.value = user.copy(nickname = user.nickname.replace("\"", ""))
                    nickname = user.nickname
                    _userImg.update { userInfo.value?.profileUrl }
                }
        }
    }

    fun setUserImg(uri: String) {
        _userImg.update { uri }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    mainRepository.deleteAccount()
                    mainRepository.emptyToken()
                    loginRepository.deleteAllData()
                }
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "delete error ${e.message}")
                throw e
            }

        }
    }

    fun logoutAccount() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    mainRepository.emptyToken()
                    loginRepository.deleteAllData()
                }
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "delete error ${e.message}")
                throw e
            }
        }
    }

    fun changeEditMode() {
        _isEditMode.value = !_isEditMode.value
    }

    fun changeText(text: Editable) {
        nickname = text.toString()
    }

    fun saveUser(img: MultipartBody.Part? = null) {
        viewModelScope.launch {
            if (nickname.isEmpty()) {
                _showToast.emit("닉네임이 비어있습니다.")
                return@launch
            } else if (!("^[a-zA-Z0-9ㄱ-ㅎ가-힣]+$".toRegex()).matches(nickname)) {
                _showToast.emit("닉네임엔 영어와 한글, 숫자만 사용가능합니다.")
                return@launch
            }
            mainRepository.patchUser(nickname, img)
                .catch {
                    _isEditMode.value = false
                }
                .collectLatest {
                    _isEditMode.value = false
                }
        }
    }

    fun getUserImageRemove() {
        viewModelScope.launch {
            try {
                mainRepository.getUserImageRemove()
                mainRepository.getMyInfo()
                    .catch {
                        Log.d("PLANJDEBUG", "initUser error ${it.message}")
                    }
                    .collectLatest { user ->
                        Log.d("PLANJDEBUG", "getUserImageRemove getMyInfo $user")
                        _userInfo.value = user.copy(nickname = user.nickname.replace("\"", ""))
                        nickname = user.nickname
                        _userImg.update { userInfo.value?.profileUrl }
                    }
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "getUserImageRemove ${e.message}")
                e.message?.let {
                    when {
                        it.contains("401") -> {}
                        it.contains("404") -> {}
                    }
                }
            }
        }
    }

    fun getAllSchedules() {
        viewModelScope.launch {
            mainRepository.getSearchSchedules("").catch {
                Log.d("PLANJDEBUG", "getTotalSchedules Error ${it.message}")
            }.collectLatest {
                Log.d("PLANJDEBUG", "getTotalSchedules Success")
                _totalSchedules.value = it
            }
        }
    }
}