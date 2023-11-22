package com.boostcamp.planj.ui.main.friendlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.planj.data.model.User

class FriendAdapter :
    ListAdapter<User, FriendAdapterViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendAdapterViewHolder {
        return FriendAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FriendAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.email == newItem.email
            }
        }
    }
}