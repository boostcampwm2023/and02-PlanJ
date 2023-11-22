package com.boostcamp.planj.ui.main.friendlist

import com.boostcamp.planj.data.model.User

interface FriendClickListener {

    fun onClick(user: User)
}