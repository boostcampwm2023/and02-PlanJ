package com.boostcamp.planj.ui.schedule

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Alarm
import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.databinding.FragmentScheduleBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class ScheduleFragment : Fragment(), RepetitionSettingDialogListener, AlarmSettingDialogListener {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by activityViewModels()

    private val args: ScheduleFragmentArgs by navArgs()

    private val repetitionSettingDialog by lazy {
        RepetitionSettingDialog()
    }

    private val alarmSettingDialog by lazy {
        AlarmSettingDialog()
    }

    private val participantDialog by lazy {
        ScheduleParticipantDialog()
    }

    private val datePickerBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }

    private val timePickerBuilder by lazy {
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        initAdapter()
        setObserver()
        setListener()

        viewModel.setLocation(args.startLocation, args.location)
        repetitionSettingDialog.setRepetitionDialogListener(this)
        alarmSettingDialog.setAlarmSettingDialogListener(this)

        binding.executePendingBindings()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun initAdapter() {
        val adapter = ScheduleParticipantAdapter(viewModel.members.value)
        binding.rvScheduleParticipants.adapter = adapter
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isEditMode.collect { isEditMode ->
                updateToolbar(isEditMode)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isComplete.collect { isComplete ->
                if (isComplete) activity?.finish()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categoryList.collect { categoryList ->
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.item_dropdown, categoryList)
                binding.actvScheduleSelectedCategory.setAdapter(arrayAdapter)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scheduleAlarm.collect { alarm ->

                    if (alarm != null && alarm.alarmType == "DEPARTURE") {
                        binding.tvScheduleLocationAlarm.text = "위치 알람 해제"
                        binding.tvScheduleLocationAlarm.setBackgroundResource(R.drawable.round_r8_red)

                    } else {
                        binding.tvScheduleLocationAlarm.text = "위치 알람 설정"
                        binding.tvScheduleLocationAlarm.setBackgroundResource(R.drawable.round_r8_main2)
                    }
                }
            }
        }

    }

    private fun setListener() {
        binding.toolbarSchedule.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_schedule_edit -> {
                    viewModel.startEditingSchedule()
                    true
                }

                R.id.item_schedule_delete -> {
                    viewModel.deleteSchedule()
                    activity?.finish()
                    true
                }

                R.id.item_schedule_complete -> {
                    viewModel.completeEditingSchedule()
                    true
                }

                else -> {
                    false
                }
            }
        }

        binding.toolbarSchedule.setNavigationOnClickListener {
            activity?.finish()
        }

        binding.tvScheduleStartEmpty.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getStartDate())
            datePicker.show(childFragmentManager, "시작 날짜 설정")
            val timePicker = setTimePicker(viewModel.scheduleStartTime.value)
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setStartDate(datePicker.selection ?: 0)
                timePicker.show(childFragmentManager, "시작 시간 설정")
            }
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setStartTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleStartDate.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getStartDate())
            datePicker.show(childFragmentManager, "시작 날짜 설정")
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setStartDate(datePicker.selection ?: 0)
            }
        }

        binding.tvScheduleEndDate.setOnClickListener {
            val datePicker = setDatePicker(viewModel.getEndDate())
            datePicker.show(childFragmentManager, "종료 날짜 설정")
            datePicker.addOnPositiveButtonClickListener {
                viewModel.setEndDate(datePicker.selection ?: 0)
            }
        }

        binding.tvScheduleStartTime.setOnClickListener {
            val timePicker = setTimePicker(viewModel.scheduleStartTime.value)
            timePicker.show(childFragmentManager, "시작 시간 설정")
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setStartTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleEndTime.setOnClickListener {
            val timePicker = setTimePicker(viewModel.scheduleEndTime.value)
            timePicker.show(childFragmentManager, "종료 시간 설정")
            timePicker.addOnPositiveButtonClickListener {
                viewModel.setEndTime(timePicker.hour, timePicker.minute)
            }
        }

        binding.tvScheduleRepetitionSetting.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("repetitionInfo", viewModel.scheduleRepetition.value)
            repetitionSettingDialog.arguments = bundle
            if (!repetitionSettingDialog.isAdded) {
                repetitionSettingDialog.show(childFragmentManager, "반복 설정")
            }
        }

        binding.tvScheduleAlarmSetting.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("alarmInfo", viewModel.scheduleAlarm.value)
            alarmSettingDialog.arguments = bundle
            if (!alarmSettingDialog.isAdded) {
                alarmSettingDialog.show(childFragmentManager, "알림 설정")
            }
        }

        binding.ivScheduleMap.setOnClickListener {
            val action =
                ScheduleFragmentDirections.actionScheduleFragmentToScheduleMapFragment(viewModel.endScheduleLocation.value)
            findNavController().navigate(action)
        }

        binding.ivScheduleStartMap.setOnClickListener {
            val action =
                ScheduleFragmentDirections.actionScheduleFragmentToScheduleStartMapFragment(
                    endLocation = viewModel.endScheduleLocation.value,
                    startLocation = viewModel.startScheduleLocation.value
                )
            findNavController().navigate(action)
        }

        binding.tvScheduleAllParticipants.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList("participantsInfo", ArrayList(viewModel.members.value))
            participantDialog.arguments = bundle
            if (!participantDialog.isAdded) {
                participantDialog.show(childFragmentManager, "전체 참가자")
            }
        }

        binding.tvScheduleLocationUrlScheme.setOnClickListener {
            activity?.let {

                val startLocation =
                    viewModel.startScheduleLocation.value ?: return@setOnClickListener
                val endLocation = viewModel.endScheduleLocation.value ?: return@setOnClickListener
                val url =
                    "nmap://route/public?slat=${startLocation.latitude}&slng=${startLocation.longitude}&sname=${startLocation.placeName}&dlat=${endLocation.latitude}&dlng=${endLocation.longitude}&dname=${endLocation.placeName}&appname=com.boostcamp.planj";

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                try {
                    it.startActivity(intent)
                } catch (e: Exception) {
                    if (e is ActivityNotFoundException) {
                        it.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.nhn.android.nmap")
                            )
                        )
                    } else {
                        Log.d("PLANJDEBUG", "${e.message}")
                    }
                }

            }
        }

        binding.tvScheduleLocationAlarm.setOnClickListener {
            viewModel.route.value?.let {
                if (binding.tvScheduleLocationAlarm.text == "위치 알람 해제") {
                    viewModel.setAlarm(null)
                } else {
                    val bottomSheet =
                        ScheduleBottomSheetDialog(it.route.trafast[0].summary.duration) { min ->
                            viewModel.setAlarm(Alarm("DEPARTURE", min))
                        }
                    bottomSheet.show(childFragmentManager, tag)
                }
            }
        }
    }

    private fun updateToolbar(isEditMode: Boolean) {
        with(binding.toolbarSchedule.menu) {
            findItem(R.id.item_schedule_edit).isVisible = !isEditMode
            findItem(R.id.item_schedule_delete).isVisible = !isEditMode
            findItem(R.id.item_schedule_complete).isVisible = isEditMode
        }
        binding.tvScheduleTop.text = if (!isEditMode) "일정" else "일정 편집"
    }

    private fun setDatePicker(selectedDate: Long?): MaterialDatePicker<Long> {
        if (selectedDate != null) {
            datePickerBuilder.setSelection(selectedDate)
        }
        return datePickerBuilder.build()
    }

    private fun setTimePicker(selectedTime: String?): MaterialTimePicker {
        if (selectedTime == null) {
            timePickerBuilder.setHour(0)
            timePickerBuilder.setMinute(0)
        } else {
            timePickerBuilder.setHour(selectedTime.split(":")[0].toInt())
            timePickerBuilder.setMinute(selectedTime.split(":")[1].toInt())
        }
        return timePickerBuilder.build()
    }

    override fun onClickComplete(repetition: Repetition?) {
        viewModel.setRepetition(repetition)
    }

    override fun onClickComplete(alarm: Alarm?) {
        viewModel.setAlarm(alarm)
    }
}