package com.boostcamp.planj.ui

import android.content.Intent
import android.util.Log
import android.widget.RemoteViewsService
import com.boostcamp.planj.data.repository.ServiceRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class PlanjViewService : RemoteViewsService() {

    @Inject
    lateinit var serviceRepository: ServiceRepository

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        val current = LocalDate.now()
        val date = String.format(
            "%04d-%02d-%02dT00:00:00",
            current.year, current.monthValue, current.dayOfMonth
        )

        return PlanjRemoteViewsFactory(this.applicationContext) { data ->
            data.clear()
            CoroutineScope(Dispatchers.IO).launch {
                serviceRepository.getDailySchedule(date)
                    .catch {
                        Log.d("PLANJDEBUG1234", "error service data ${it.message}")
                        data.addAll(emptyList())
                    }
                    .collectLatest {
                        data.addAll(it.filter { s -> !s.isFinished && !s.isFailed })
                    }
            }

        }
    }
}