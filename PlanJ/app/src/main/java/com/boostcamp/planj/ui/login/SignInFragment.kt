package com.boostcamp.planj.ui.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.boostcamp.planj.BuildConfig
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentSignInBinding
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val token = NaverIdLoginSDK.getAccessToken()
                    token?.let {
                        Log.d("PLANJDEBUG", "${token}")
                        viewModel.postSignInNaver(it)
                    } ?: Log.d("PLANJDEBUG", "naver Login Token null")
                }

                Activity.RESULT_CANCELED -> {
                    // 실패 or 에러
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.lifecycleScope.launch {
            val user = viewModel.getToken()
            if(user.isNotEmpty()){
                val action = SignInFragmentDirections.actionSignInFragmentToMainActivity()
                findNavController().navigate(action)
                requireActivity().finish()
            }
        }

        NaverIdLoginSDK.initialize(
            requireContext(),
            "${BuildConfig.NAVER_LOGIN_CLIENT_ID}",
            "${BuildConfig.NAVER_LOGIN_SECRET}",
            "PlanJ"
        )

        setObserver()
        setListener()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSuccess.collect { isSuccess ->
                if (isSuccess) {
                    findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
                    requireActivity().finish()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showToast.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setListener() {
        binding.btnSignInSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment2)
        }
        binding.ivSignInNaver.setOnClickListener {
            NaverIdLoginSDK.authenticate(requireContext(), launcher)
        }
    }
}