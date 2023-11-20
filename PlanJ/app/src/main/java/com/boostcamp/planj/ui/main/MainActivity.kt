package com.boostcamp.planj.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ContentInfoCompat.Flags
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.ActivityMainBinding
import com.boostcamp.planj.ui.schedule.ScheduleActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setJetpackNavigation()

        floatingButtonVisibleListener()

        binding.fbAddSchedule.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    private fun floatingButtonVisibleListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_home, R.id.fragment_list -> {
                    binding.fbAddSchedule.visibility = View.VISIBLE
                }

                else -> binding.fbAddSchedule.visibility = View.GONE
            }
        }
    }

    private fun setJetpackNavigation() {
        val host =
            supportFragmentManager.findFragmentById(R.id.planj_nav_host_fragment) as NavHostFragment
        navController = host.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }


}