package com.boostcamp.planj.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.planj.data.model.Participant
import com.boostcamp.planj.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FriendState(
    val participant: Participant,
    val isParticipated: Boolean
)

@HiltViewModel
class ScheduleParticipantsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private val _participantList = MutableStateFlow<List<Participant>>(emptyList())
    val participantList = _participantList.asStateFlow()

    private val _friendStateList = MutableStateFlow<List<FriendState>>(emptyList())
    val friendStateList = _friendStateList.asStateFlow()

    fun setParticipants(participants: List<Participant>) {
        _participantList.value = participants
        setFriendStateList()
    }

    private fun setFriendStateList() {
        viewModelScope.launch {
            val friendParticipatedState = mutableListOf<FriendState>()
            mainRepository.getFriendsApi()
                .catch {
                    Log.d(
                        "PLANJDEBUG",
                        "scheduleParticipantsViewModel getFriends error ${it.message}"
                    )
                }.collectLatest { friends ->
                    friends.forEach { user ->
                        val participant = _participantList.value.find { it.email == user.email }
                        if (participant == null) {
                            friendParticipatedState.add(
                                FriendState(
                                    Participant(
                                        nickname = user.nickname,
                                        email = user.email,
                                        profileUrl = user.profileUrl,
                                        isFinished = false,
                                        currentUser = false
                                    ), false
                                )
                            )
                        }
                    }
                    _participantList.value.forEach { participant ->
                        friendParticipatedState.add(FriendState(participant, true))
                    }
                    _friendStateList.value = friendParticipatedState
                }
        }
    }

    fun setEditMode() {
        _isEditMode.value = !_isEditMode.value
    }

    fun addParticipant(participant: Participant) {
        _participantList.update { participants ->
            participants + participant
        }
    }

    fun deleteParticipant(participant: Participant) {
        _participantList.update { participants ->
            participants.filter { it.email != participant.email }
        }
    }
}