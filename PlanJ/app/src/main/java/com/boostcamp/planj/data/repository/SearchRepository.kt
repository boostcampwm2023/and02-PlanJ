package com.boostcamp.planj.data.repository

import com.boostcamp.planj.data.model.Address

interface SearchRepository {

    suspend fun searchMap(
        query: String,
    ): List<Address>
}