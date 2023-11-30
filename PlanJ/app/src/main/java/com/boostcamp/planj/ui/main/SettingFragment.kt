package com.boostcamp.planj.ui.main

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentSettingBinding
import com.boostcamp.planj.ui.PlanjAlarm
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    val viewModel: SettingViewModel by viewModels()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val file = File(absolutelyPath(uri, requireContext()))
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                viewModel.setImageFile(
                    MultipartBody.Part.createFormData(
                        "profileImage",
                        file.name,
                        requestFile
                    )
                )

                Glide.with(this)
                    .load(uri)
                    .error(R.drawable.ic_circle_person)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivSettingImg)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val planjAlarm by lazy {
        PlanjAlarm(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.initUser()

        binding.ivSettingIconCamera.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.tvSettingLogout.setOnClickListener {
            runBlocking {
                viewModel.logoutAccount()
                val packageManager: PackageManager = requireContext().packageManager
                val intent = packageManager.getLaunchIntentForPackage(requireContext().packageName)
                val componentName = intent!!.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                requireContext().startActivity(mainIntent)
                activity?.finish()
            }
        }

        binding.tvSettingWithdrawal.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("회원 탈퇴")
                .setMessage("정말로 회원 탈퇴를 하시겠습니까?")
                .setNegativeButton("아니요") { _, _ -> }
                .setPositiveButton("네") { _, _ ->
                    try {
                        runBlocking {
                            viewModel.deleteAccount()
                            val packageManager: PackageManager = requireContext().packageManager
                            val intent =
                                packageManager.getLaunchIntentForPackage(requireContext().packageName)
                            val componentName = intent!!.component
                            val mainIntent = Intent.makeRestartActivityTask(componentName)
                            requireContext().startActivity(mainIntent)
                            activity?.finish()
                        }
                    } catch (e: Exception) {
                        Log.d("PLANJDEBUG", "delete error")
                    }
                }

            dialog.background = resources.getDrawable(R.drawable.round_r8_main2, null)
            dialog.show()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isEditMode.collectLatest { isMode ->
                    if (isMode) {
                        val focusView = binding.tvSettingNickname
                        focusView.isEnabled = true
                        focusView.requestFocus()
                        focusView.setSelection(focusView.text.toString().length)
                        val imm =
                            context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(focusView, 0)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfo.collectLatest { user ->
                    Glide.with(this@SettingFragment)
                        .load(user?.imgUrl)
                        .error(R.drawable.ic_circle_person)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.ivSettingImg)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAlarmOn.collectLatest { isAlarmOn ->
                    if (isAlarmOn) {
                        // TODO: 일회성/반복성, 현재시간/알람시간 모두 비교 필요
                        // 무조건 알람을 set하면 시간이 지난 알람은 바로 울림
                        viewModel.getAllAlarmInfo()
                            .forEach { alarmInfo -> planjAlarm.setAlarm(alarmInfo) }
                    } else {
                        viewModel.getAllAlarmInfo()
                            .forEach { alarmInfo -> planjAlarm.deleteAlarm(alarmInfo.scheduleId.hashCode()) }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    // 절대경로 변환
    private fun absolutelyPath(path: Uri?, context: Context): String {
        if (path == null) return ""
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        val result = c?.getString(index!!)

        return result ?: ""
    }
}