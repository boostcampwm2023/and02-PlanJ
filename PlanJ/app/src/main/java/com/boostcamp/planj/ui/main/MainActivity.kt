package com.boostcamp.planj.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.ActivityMainBinding
import com.boostcamp.planj.ui.schedule.ScheduleActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
        setJetpackNavigation()

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
                } else {
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
    }

    companion object {
        val REQUEST_CODE = 100
    }

}