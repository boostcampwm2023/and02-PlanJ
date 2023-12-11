package com.boostcamp.planj.ui.main.friendlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.planj.data.model.User
import com.boostcamp.planj.databinding.FragmentFriendListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendListFragment : Fragment() {

    private var _binding: FragmentFriendListBinding? = null
    private val binding get() = _binding!!
    val viewModel: FriendListViewModel by viewModels()

    private lateinit var friendAdapter: FriendAdapter

    val bundle by lazy {
        Bundle()
    }

    private val friendInfoDialog by lazy {
        FriendInfoDialog()
    }

    private val addFriendDialog by lazy {
        AddFriendDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFriends()

        initAdapter()
        setObserver()
        setListener()
        setAddDialogListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        val listener = object : FriendClickListener {
            override fun onClick(user: User) {
                bundle.putParcelable("user", user)
                friendInfoDialog.arguments = bundle
                friendInfoDialog.show(childFragmentManager, "친구 정보")
            }

            override fun onDelete(email: String) {
                viewModel.deleteUser(email)
            }
        }
        friendAdapter = FriendAdapter(listener)
        binding.rvFriendListFriends.adapter = friendAdapter
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendList.collectLatest { friendList ->
                    friendAdapter.submitList(friendList)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showToast.collectLatest { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setListener() {
        binding.layoutFriendListAdd.setOnClickListener {
            if (!addFriendDialog.isAdded) {
                addFriendDialog.show(childFragmentManager, "친구 추가")
            }
        }
    }

    private fun setAddDialogListener() {
        val listener = object : AddFriendDialogListener {
            override fun onClickComplete(email: String) {
                viewModel.addUser(email)
            }
        }
        addFriendDialog.setAddFriendDialogListener(listener)
    }
}