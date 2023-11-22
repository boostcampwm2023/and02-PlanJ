package com.boostcamp.planj.ui.main.friendlist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.databinding.DialogFriendInfoBinding

class FriendInfoDialog : DialogFragment() {

    private var _binding: DialogFriendInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFriendInfoBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("user", User::class.java)
        } else {
            arguments?.getParcelable("user")
        }
        binding.user = user
        setListener()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setListener() {
        binding.ivDialogFriendClose.setOnClickListener {
            dismiss()
        }
    }
}