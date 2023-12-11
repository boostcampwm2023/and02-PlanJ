package com.boostcamp.planj.ui.schedule

import com.boostcamp.planj.data.model.Address

fun interface SearchMapClickListener {
    fun onClick(address: Address)
}