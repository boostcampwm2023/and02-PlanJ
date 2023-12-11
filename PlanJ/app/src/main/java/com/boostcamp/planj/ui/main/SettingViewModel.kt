package com.boostcamp.planj.ui.main

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.AlarmInfo
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private var imageFile: MultipartBody.Part? = null
    private var nickName: String = ""

    private val _isAlarmOn = MutableStateFlow(true)
    val isAlarmOn = _isAlarmOn.asStateFlow()

    private val _userInfo = MutableStateFlow<User?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private val _showToast = MutableSharedFlow<String>()
    val showToast = _showToast.asSharedFlow()

    private val _totalSchedules = MutableStateFlow<List<Schedule>>(emptyList())
    val totalSchedules = _totalSchedules.asStateFlow()

    private var _completeCount = MutableStateFlow(0)
    val completeCount = _completeCount.asStateFlow()

    private var _failCount = MutableStateFlow(0)
    val failCount = _failCount.asStateFlow()

    private var _haveCount = MutableStateFlow(0)
    val haveCount = _haveCount.asStateFlow()

    init {
        viewModelScope.launch {
            _isAlarmOn.value = getAlarmMode()
        }
    }

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
                    nickName = user.nickname
                }
        }
    }

    fun onClickAlarmSwitch() {
        _isAlarmOn.value = !_isAlarmOn.value
        saveAlarmMode(_isAlarmOn.value)
    }

    fun getAllAlarmInfo(): List<AlarmInfo> {
        var alarmList: List<AlarmInfo> = emptyList()
        viewModelScope.launch {
            loginRepository.updateAlarmInfo(System.currentTimeMillis())
            alarmList = loginRepository.getAllAlarmInfo()
        }
        return alarmList
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    mainRepository.deleteAccount()
                    mainRepository.emptyToken()
                    loginRepository.deleteAllData()
                    loginRepository.saveAlarmMode(false)
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
                    loginRepository.saveAlarmMode(false)
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
        nickName = text.toString()
    }

    fun setImageFile(imageFile: MultipartBody.Part?) {
        this.imageFile = imageFile
    }

    fun saveUser() {
        viewModelScope.launch {

            if (nickName.isEmpty()) {
                _showToast.emit("닉네임이 비어있습니다.")
                return@launch
            } else if (!("^[a-zA-Z0-9ㄱ-ㅎ가-힣]+$".toRegex()).matches(nickName)) {
                _showToast.emit("닉네임엔 영어와 한글, 숫자만 사용가능합니다.")
                return@launch
            }
            mainRepository.patchUser(nickName, imageFile)
                .catch {
                    _isEditMode.value = false
                }
                .collectLatest {
                    _isEditMode.value = false
                }
        }
    }

    private suspend fun getAlarmMode() = withContext(Dispatchers.IO) {
        loginRepository.getAlarmMode().first()
    }

    private fun saveAlarmMode(mode: Boolean) {
        viewModelScope.launch {
            loginRepository.saveAlarmMode(mode)
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
                        nickName = user.nickname
                        imageFile = null
                    }
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "getUserImageRemove ${e.message}")
                e.message?.let {
                    when{
                        it.contains("401") -> {}
                        it.contains("404") -> {}
                        it.contains("404") -> {}
                        it.contains("404") -> {}
                    }
                }
            }
        }
    }

    fun getTotalSchedules() {
        viewModelScope.launch {
            mainRepository.getSearchSchedules("").catch {
                Log.d("PLANJDEBUG", "getTotalSchedules Error ${it.message}")
            }.collectLatest {
                Log.d("PLANJDEBUG", "getTotalSchedules Success")
                _totalSchedules.value = it
                _completeCount.value =
                    _totalSchedules.value.filter { schedule: Schedule -> schedule.isFinished && !schedule.isFailed }.size
                _failCount.value =
                    _totalSchedules.value.filter { schedule: Schedule -> schedule.isFailed }.size
                _haveCount.value =
                    _totalSchedules.value.filter { schedule: Schedule -> !schedule.isFinished }.size
            }
        }
    }
}