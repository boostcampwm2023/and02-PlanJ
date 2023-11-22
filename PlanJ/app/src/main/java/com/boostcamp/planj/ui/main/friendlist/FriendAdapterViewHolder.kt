package com.boostcamp.planj.ui.main.friendlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.databinding.ItemFriendBinding

class FriendAdapterViewHolder(private val binding: ItemFriendBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.user = user
    }

    companion object {
        fun from(parent: ViewGroup): FriendAdapterViewHolder {
            return FriendAdapterViewHolder(
                ItemFriendBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}