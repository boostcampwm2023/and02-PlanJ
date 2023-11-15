package com.boostcamp.planj.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        setListener()
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSuccess.collect { isSuccess ->
                if (isSuccess) findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
            }
        }
    }

    private fun setListener() {
        binding.btnSignInSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment2)
        }
    }
}