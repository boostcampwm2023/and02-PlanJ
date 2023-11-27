package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.naver.NaverResponse
import kotlinx.coroutines.flow.Flow

interface NaverRepository {
    suspend fun getNaverRoute(start : String, goal : String) : NaverResponse
}