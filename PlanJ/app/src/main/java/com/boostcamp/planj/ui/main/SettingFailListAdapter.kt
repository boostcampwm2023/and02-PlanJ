package com.boostcamp.planj.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.boostcamp.planj.data.model.FailedMemo
import com.boostcamp.planj.databinding.ItemFailMemoBinding

class SettingFailListAdapter(
    private val context: Context,
    private val list: List<FailedMemo>
) : BaseAdapter() {

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItem(position: Int): FailedMemo = list[position]

    override fun getCount(): Int = list.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemFailMemoBinding.inflate(LayoutInflater.from(context))
        binding.data = list[position]
        return binding.root
    }
}