package com.boostcamp.planj.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentShowMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.coroutines.launch

class ScheduleShowMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentShowMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by activityViewModels()

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private val startMarker = Marker()
    private val endMarker = Marker()
    private val path = PathOverlay()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.fragmentContainMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        startMarker.width = 70
        startMarker.height = 100
        endMarker.width = 70
        endMarker.height = 100

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
        naverMap.uiSettings.isZoomGesturesEnabled = false
        naverMap.uiSettings.isLogoClickEnabled = false
        naverMap.uiSettings.isScrollGesturesEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isScaleBarEnabled = false
        setObserver()
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.endScheduleLocation.collect { el ->
                    Log.d("PLANJDEBUG", "${el}")
                    if (el != null) {
                        val latLng = LatLng(el.latitude.toDouble(), el.longitude.toDouble())
                        endMarker.position = latLng
                        endMarker.map = naverMap

                        val camera = CameraUpdate.scrollTo(latLng)
                            .animate(CameraAnimation.Linear)
                        naverMap.moveCamera(camera)
                    } else {
                        endMarker.map = null
                        viewModel.emptyStartLocation()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startScheduleLocation.collect { sl ->
                    if (sl != null) {
                        val endLocation = viewModel.endScheduleLocation.value
                        if (endLocation != null) {
                            val latLng = LatLng(sl.latitude.toDouble(), sl.longitude.toDouble())
                            startMarker.position = latLng
                            startMarker.map = naverMap
                            viewModel.getNaverRoute(sl, endLocation)
                        }
                    } else {
                        startMarker.map = null
                        viewModel.emptyRoute()
                        if (endMarker.map != null) {
                            viewModel.endScheduleLocation.value?.let { el ->
                                val latLng = LatLng(el.latitude.toDouble(), el.longitude.toDouble())
                                val camera = CameraUpdate.scrollTo(latLng)
                                    .animate(CameraAnimation.Linear)
                                naverMap.moveCamera(camera)
                            }
                        }
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.response.collect { route ->
                    route?.let { response ->
                        val latLngList = response.route.trafast[0].path.map { position ->
                            LatLng(
                                position[1],
                                position[0]
                            )
                        }
                        path.map = null
                        path.coords = latLngList
                        path.color = resources.getColor(R.color.red, null)
                        path.map = naverMap

                        val start = LatLng(
                            viewModel.endScheduleLocation.value!!.latitude.toDouble(),
                            viewModel.endScheduleLocation.value!!.longitude.toDouble()
                        )
                        val end = LatLng(
                            viewModel.startScheduleLocation.value!!.latitude.toDouble(),
                            viewModel.startScheduleLocation.value!!.longitude.toDouble()
                        )
                        val bounds = LatLngBounds(start, end)
                        val camera =
                            CameraUpdate.fitBounds(bounds, 150).animate(CameraAnimation.Linear)
                        naverMap.moveCamera(camera)
                    }
                    if (route == null) {
                        path.map = null
                    }
                }
            }


        }
    }
}