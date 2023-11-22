package com.boostcamp.planj.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.boostcamp.planj.ui.main.week.WeekFragment

class MyFragmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragment: List<Fragment> = listOf(TodayFragment(), WeekFragment())

    override fun createFragment(position: Int): Fragment {
        return fragment[position]
    }

    override fun getItemCount(): Int = fragment.size
}