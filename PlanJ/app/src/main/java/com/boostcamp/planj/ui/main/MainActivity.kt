package com.boostcamp.planj.ui.main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.ActivityMainBinding
import com.boostcamp.planj.ui.schedule.ScheduleDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setJetpackNavigation()

        floatingButtonVisibleListener()

        binding.fbAddSchedule.setOnClickListener {
            val dialog = ScheduleDialog(this)
            dialog.show()
            dialog.setAddSchedule {
                Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                viewModel.insertSchedule(it)
            }
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