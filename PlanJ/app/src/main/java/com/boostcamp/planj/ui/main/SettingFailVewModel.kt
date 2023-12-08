package com.boostcamp.planj.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.FailedMemo
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingFailVewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _failedMemo = MutableStateFlow<List<FailedMemo>>(emptyList())
    val failedMemo = _failedMemo.asStateFlow()

    fun getFailedMemo() {
        viewModelScope.launch {
            mainRepository.getFailedMemo()
                .catch {
                    Log.d("PLANJDEBUG", "getFailedMemo error ${it.message}")
                }
                .collectLatest {
                    _failedMemo.value = it
                }
        }
    }
}