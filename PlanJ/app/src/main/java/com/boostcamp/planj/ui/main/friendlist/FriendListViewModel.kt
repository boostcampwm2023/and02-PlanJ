package com.boostcamp.planj.ui.main.friendlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.model.dto.DeleteFriendBody
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _friendList = MutableStateFlow<List<User>>(emptyList())
    val friendList = _friendList.asStateFlow()

    private val _showToast = MutableSharedFlow<String>()
    val showToast = _showToast.asSharedFlow()

    fun getFriends() {
        viewModelScope.launch {
            mainRepository.getFriendsApi()
                .catch {
                    Log.d("PLANJDEBUG", "friendViewModel error ${it.message}")
                    _showToast.emit("친구 조회를 실패했습니다.")
                }.collectLatest { friends ->
                    _friendList.value = friends
                }
        }
    }

    fun addUser(email: String) {
        viewModelScope.launch {
            mainRepository.postFriendApi(email)
                .catch {
                    _showToast.emit("친구 추가를 실패했습니다.")
                    Log.d("PLANJDEBUG", "friendViewModel error ${it.message}")
                }
                .collectLatest { message ->
                    _showToast.emit(message)
                    getFriends()
                }
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            try {
                mainRepository.deleteFriendApi(email)
                _showToast.emit("친구 삭제를 완료했습니다.")
                getFriends()
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "friendViewModel error ${e.message}")
                _showToast.emit("친구 삭제를 실패했습니다.")
            }
        }
    }
}