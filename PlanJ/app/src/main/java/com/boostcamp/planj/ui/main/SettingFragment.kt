package com.boostcamp.planj.ui.main

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentSettingBinding
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
                Log.d("PLANJDEBUG", "url : ${uri}")
                viewModel.setUserImg(uri.toString())
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private lateinit var notificationManager: NotificationManagerCompat

    private val getNotificationSettingResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.isAlarmEnable = notificationManager.areNotificationsEnabled()
            }
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

        notificationManager = NotificationManagerCompat.from(requireContext())
        binding.isAlarmEnable = notificationManager.areNotificationsEnabled()

        viewModel.setMode()
        viewModel.initUser()
        viewModel.getAllSchedules()

        setObserver()
        setListener()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setObserver() {
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
                viewModel.showToast.collectLatest { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setListener() {
        binding.ivSettingIconCamera.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.ivSettingIconCancel.setOnClickListener {
            viewModel.getUserImageRemove()
        }

        binding.tvSettingReadFailMemo.setOnClickListener {
            val action = SettingFragmentDirections.actionFragmentUserToSettingFailFragment()
            findNavController().navigate(
                action,
                navOptions { // Use the Kotlin DSL for building NavOptions

                })
        }

        binding.ivSettingIconCheck.setOnClickListener {
            viewModel.userImg.value?.let { userImg ->
                val file = File(absolutelyPath(Uri.parse(userImg), requireContext()))
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                viewModel.saveUser(
                    MultipartBody.Part.createFormData(
                        "profileImage",
                        file.name,
                        requestFile
                    )
                )
            } ?: viewModel.saveUser()
        }

        binding.tvSettingLogout.setOnClickListener {
            runBlocking {
                viewModel.logoutAccount()
            }

            val packageManager: PackageManager = requireContext().packageManager
            val intent = packageManager.getLaunchIntentForPackage(requireContext().packageName)
            val componentName = intent!!.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity?.startActivity(mainIntent)
            activity?.finish()
        }

        binding.layoutSettingAlarm.setOnClickListener {
            val appDetail = Intent(
                Settings.ACTION_APP_NOTIFICATION_SETTINGS
            ).apply {
                this.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            getNotificationSettingResult.launch(appDetail)
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
                        }
                        val packageManager: PackageManager = requireContext().packageManager
                        val intent =
                            packageManager.getLaunchIntentForPackage(requireContext().packageName)
                        val componentName = intent!!.component
                        val mainIntent = Intent.makeRestartActivityTask(componentName)
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        activity?.startActivity(mainIntent)
                        activity?.finish()

                    } catch (e: Exception) {
                        Log.d("PLANJDEBUG", "delete error")
                    }
                }

            dialog.background = resources.getDrawable(R.drawable.round_r8_main2, null)
            dialog.show()
        }
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