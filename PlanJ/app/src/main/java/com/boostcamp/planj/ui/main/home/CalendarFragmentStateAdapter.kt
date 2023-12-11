package com.boostcamp.planj.ui.main.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarFragmentStateAdapter(
    fragmentActivity: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentActivity, lifecycle) {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        return CalendarFragment().apply {
            pageIndex = position - Int.MAX_VALUE / 2
        }
    }


}