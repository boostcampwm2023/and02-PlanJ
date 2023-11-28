package com.boostcamp.planj.ui.schedule

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.boostcamp.planj.data.model.Location
import com.boostcamp.planj.databinding.FragmentScheduleMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class ScheduleMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentScheduleMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleMapViewModel by viewModels()
    private val args: ScheduleMapFragmentArgs by navArgs()

    private lateinit var adapter: ScheduleSearchAdapter
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private val marker = Marker()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.fragmentContainMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setListener()
        getFocus()
        initAdapter()
        setObserver()
        mapSearch()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0
        naverMap.setOnMapClickListener { _, latLng ->
            getMarker(latLng)
        }
        viewModel.initLocation(args.location)
    }

    private fun setListener() {
        binding.tilScheduleMapSearch.setEndIconOnClickListener {
            marker.map = null
            adapter.submitList(emptyList())
            binding.tietScheduleMapSearchInput.setText("")
            viewModel.setLocation(null)

        }
        binding.btnScheduleMapSelectPlace.setOnClickListener {
            val action =
                ScheduleMapFragmentDirections.actionScheduleMapFragmentToScheduleFragment(location = viewModel.location.value)
            findNavController().navigate(action)
        }
    }

    private fun getFocus() {
        binding.tietScheduleMapSearchInput.requestFocus()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.tietScheduleMapSearchInput.windowInsetsController?.show(
                WindowInsetsCompat.Type.ime()
            )
        } else {
            activity?.let {
                WindowInsetsControllerCompat(it.window, binding.tietScheduleMapSearchInput)
                    .show(WindowInsetsCompat.Type.ime())
            }
        }
    }

    private fun getMarker(latLng: LatLng) {
        marker.map = null
        marker.position = latLng
        marker.map = naverMap

        getAddress(latLng)
    }

    private fun getAddress(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.KOREAN)

        // 안드로이드 API 레벨이 33 이상인 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1
            ) { address ->
                if (address.size != 0) {
                    viewModel.setLocation(
                        Location(
                            address[0].getAddressLine(0),
                            address[0].getAddressLine(0),
                            latLng.latitude.toString(),
                            latLng.longitude.toString()
                        )
                    )
                }
            }
        } else { // API 레벨이 33 미만인 경우
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null) {
                viewModel.setLocation(
                    Location(
                        addresses[0].getAddressLine(0),
                        addresses[0].getAddressLine(0),
                        latLng.latitude.toString(),
                        latLng.longitude.toString()
                    )
                )
            }
        }
    }

    private fun initAdapter() {
        val clickListener = SearchMapClickListener {
            viewModel.setLocation(
                Location(it.placeName, it.addressName, it.y, it.x)
            )
            viewModel.changeClick()
        }

        adapter = ScheduleSearchAdapter(clickListener)
        binding.rvScheduleMapSearchList.adapter = adapter
        binding.rvScheduleMapSearchList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.submitList(emptyList())
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchMap.collectLatest {
                    if (it.isEmpty() || viewModel.clicked.value) {
                        adapter.submitList(emptyList())
                    } else {
                        adapter.submitList(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clicked.collectLatest {
                    if (it) {
                        adapter.submitList(emptyList())
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.location.collectLatest {
                    it?.let { location ->
                        if (location.address.isNotEmpty()) {
                            binding.tietScheduleMapSearchInput.setText(location.placeName)
                            binding.tietScheduleMapSearchInput.setSelection(location.placeName.length)
                            val camera = CameraUpdate.scrollTo(
                                LatLng(
                                    location.latitude.toDouble(),
                                    location.longitude.toDouble()
                                )
                            )
                                .animate(CameraAnimation.Linear)
                            naverMap.moveCamera(camera)
                            marker.map = null
                            marker.position =
                                LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                            marker.map = naverMap
                        }
                    }
                }
            }
        }
    }

    private fun mapSearch() {
        binding.tietScheduleMapSearchInput.addTextChangedListener { text ->
            text?.let {
                val query = it.toString().trim()
                if (viewModel.clicked.value) {
                    viewModel.changeClick()
                }
                if (query.isNotEmpty()) {
                    viewModel.searchMap(query)
                } else {
                    adapter.submitList(emptyList())
                }
            }
        }
    }
}