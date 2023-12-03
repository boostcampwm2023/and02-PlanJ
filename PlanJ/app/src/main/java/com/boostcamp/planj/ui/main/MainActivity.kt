package com.boostcamp.planj.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.ActivityMainBinding
import com.boostcamp.planj.ui.schedule.ScheduleDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    private var categoryList = emptyList<Category>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
        setJetpackNavigation()
        floatingButtonVisibleListener()
        setListener()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collectLatest {
                    categoryList = it.filter { c -> c.categoryName != "전체 일정" }
                }
            }
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
                    Toast.makeText(this, resources.getString(R.string.post_notifications_allowed), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, resources.getString(R.string.post_notifications_denied), Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun setListener() {
        binding.fbAddSchedule.setOnClickListener {
            val dialog = ScheduleDialog(categoryList.map { it.categoryName }, "미분류") { category, title, endTime ->
                viewModel.insertSchedule(category, title, endTime)
            }
            dialog.show(
                supportFragmentManager, null
            )
        }
    }

    private fun floatingButtonVisibleListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_home, R.id.fragment_category -> {
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

    companion object{
        val REQUEST_CODE = 100
    }
}