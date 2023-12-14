package com.boostcamp.planj.ui.main.friendlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.databinding.ItemFriendBinding

class FriendAdapterViewHolder(private val binding: ItemFriendBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, listener: FriendClickListener) {
        binding.user = user
        binding.listener = listener
        val friendMenu = PopupMenu(binding.tvFriendMenu.context, binding.tvFriendMenu)
        friendMenu.inflate(R.menu.friend_menu)

        friendMenu.setOnMenuItemClickListener {
            listener.onDelete(user.email)
            true
        }

        binding.tvFriendMenu.setOnClickListener {
            friendMenu.show()
        }
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