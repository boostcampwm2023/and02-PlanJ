package com.boostcamp.planj.data.repository

import android.util.Log
import com.boostcamp.planj.data.model.naver.NaverResponse
import com.boostcamp.planj.data.network.NaverApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NaverRepositoryImpl @Inject constructor(
    private val api: NaverApi
) : NaverRepository {
    override suspend fun getNaverRoute(start: String, goal: String): NaverResponse =
        api.getNaverRoute(start,goal, "trafast")

}