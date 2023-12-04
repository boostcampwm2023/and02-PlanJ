package com.boostcamp.planj.ui.main.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarFragmentStateAdapter(private val onClickListener: OnClickListener ,fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = Int.MAX_VALUE


    override fun createFragment(position: Int): Fragment {
        return CalendarFragment(onClickListener).apply {
            pageIndex = position
        }
    }




}