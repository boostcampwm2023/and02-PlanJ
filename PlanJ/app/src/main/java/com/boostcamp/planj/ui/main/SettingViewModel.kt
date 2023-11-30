package com.boostcamp.planj.ui.main

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.repository.LoginRepository
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    private var imageFile: MultipartBody.Part? = null
    private var nickName: String = ""

    private val _isAlarmOn = MutableStateFlow(false)
    val isAlarmOn = _isAlarmOn.asStateFlow()

    private val _userInfo = MutableStateFlow<User?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    init {
        viewModelScope.launch {
            _isAlarmOn.value = getAlarmMode()
        }
    }

    fun initUser(){
        viewModelScope.launch {
            mainRepository.getMyInfo().collectLatest { user ->
                _userInfo.value = user.copy(nickname = user.nickname.replace("\"", ""))
            }
        }
    }

    fun onClickAlarmSwitch() {
        _isAlarmOn.value = !_isAlarmOn.value
        saveAlarmMode(_isAlarmOn.value)
    }

    fun onClickLogout() {

    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    mainRepository.deleteAccount()
                    mainRepository.emptyToken()
                    mainRepository.deleteAllData()
                    mainRepository.saveAlarmMode(false)
                }
            }catch (e : Exception){
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
            Log.d("PLANJDEBUG", "userName : $nickName, ImageFiel : $imageFile ")
            mainRepository.postUser(nickName, imageFile)
                .catch {
                    Log.d("PLANJDEBUG", "saveUser error ${it.message}")
                    _isEditMode.value = false
                }
                .collectLatest {
                    Log.d("PLANJDEBUG", "saveUser success $it")
                    _isEditMode.value = false
                }

        }
    }

    private suspend fun getAlarmMode() = withContext(Dispatchers.IO) {
        mainRepository.getAlarmMode().first()
    }

    private fun saveAlarmMode(mode: Boolean) {
        viewModelScope.launch {
            Log.d("PLANJDEBUG", "saveAlarmMode $mode")
            mainRepository.saveAlarmMode(mode)
        }
    }

    override fun onCleared() {
        Log.d("PLANJDEBUG", "onClear")
        super.onCleared()
    }
}