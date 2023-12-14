package com.boostcamp.planj.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.ActivityMainBinding
import com.boostcamp.planj.ui.PlanjAlarm
import com.boostcamp.planj.ui.main.home.HomeFragmentDirections
import com.boostcamp.planj.ui.schedule.ScheduleActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    private val planjAlarm by lazy {
        PlanjAlarm(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.isFirst()) {
            checkPermission()
            viewModel.saveFirst()
        }
        setJetpackNavigation()
        setObserver()
        setIntent()
    }

    private fun setIntent() {
        intent.getStringExtra("index")?.let {
            navController.popBackStack()
            when (it) {
                "friend" -> {
                    navController.navigate(R.id.fragment_friend_list)
                }

                else -> {
                    navController.navigate(R.id.fragment_home, intent.extras)
                }
            }
        }

        val scheduleId = intent.getStringExtra("scheduleId")
        if (scheduleId != null) {
            val intent = Intent(this, ScheduleActivity::class.java)
            intent.putExtra("scheduleId", scheduleId)
            startActivity(intent)
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.post_notifications_allowed),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (grantResults.isNotEmpty()) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.post_notifications_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    private fun setJetpackNavigation() {
        val host =
            supportFragmentManager.findFragmentById(R.id.planj_nav_host_fragment) as NavHostFragment
        navController = host.navController
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.visibility = if (destination.id == R.id.settingFailFragment) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deletedAlarms.collectLatest { deletedAlarms ->
                    deletedAlarms.forEach { planjAlarm.deleteAlarm(it.scheduleId) }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newAlarms.collectLatest { newAlarms ->
                    newAlarms.forEach {
                        planjAlarm.setAlarm(it)
                    }
                }
            }
        }
    }

    companion object {
        val REQUEST_CODE = 100
    }
}