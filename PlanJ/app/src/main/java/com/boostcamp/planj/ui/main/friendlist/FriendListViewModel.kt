package com.boostcamp.planj.ui.main.friendlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
            mainRepository.getFriendsApi().collectLatest { friends ->
                _friendList.value = friends
            }
        }
    }

    fun addUser(email: String) {
        viewModelScope.launch {
            try {
                mainRepository.postFriendApi(email)
                getFriends()
            } catch (e: Exception) {
                _showToast.emit("친구 추가에 실패했습니다.")
                Log.d("PLANJDEBUG", "friendViewModel error ${e.message}")
            }
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //TODO api로 변경
        }
    }
}