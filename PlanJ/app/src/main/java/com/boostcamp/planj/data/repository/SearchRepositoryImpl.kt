package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.Address
import com.boostcamp.planj.data.network.KaKaoSearchApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val api : KaKaoSearchApi
) : SearchRepository {
    override suspend fun searchMap(query: String): List<Address> {
        return api.searchMap(query).addresses
    }
}