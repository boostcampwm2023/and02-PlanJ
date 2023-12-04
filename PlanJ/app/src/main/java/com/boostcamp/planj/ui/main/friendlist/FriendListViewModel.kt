package com.boostcamp.planj.ui.main.friendlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    fun getFriends() {
        viewModelScope.launch {
            mainRepository.getFriendsApi().collectLatest { userList ->
                _userList.value = userList
            }
        }
    }

    fun addUser(email: String) {
        viewModelScope.launch {
            try {
                mainRepository.postFriendApi(email)
                getFriends()
            } catch (e: Exception) {
                Log.d("PLANJDEBUG", "friendViewModel error ${e.message}")
            }
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteUser(email)
        }
    }
}