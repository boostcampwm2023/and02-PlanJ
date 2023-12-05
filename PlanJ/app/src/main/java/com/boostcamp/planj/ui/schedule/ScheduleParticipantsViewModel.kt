package com.boostcamp.planj.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Participant
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleParticipantsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private val _participantList = MutableStateFlow<List<Participant>>(emptyList())
    val participantList = _participantList.asStateFlow()

    private val _friendList = MutableStateFlow<List<User>>(emptyList())
    val friendList = _friendList.asStateFlow()

    fun setParticipants(participants: List<Participant>){
        _participantList.value = participants
    }

    fun getFriendList() {
        viewModelScope.launch {
            mainRepository.getFriendsApi().collectLatest { friends ->
                _friendList.value = friends
            }
        }
    }

    fun setEditMode() {
        _isEditMode.value = !_isEditMode.value
    }
}