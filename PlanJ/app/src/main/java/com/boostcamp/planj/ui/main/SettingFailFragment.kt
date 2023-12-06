package com.boostcamp.planj.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.FragmentSettingFailBinding
import com.boostcamp.planj.databinding.ItemFailMemoBinding

data class DummyFailData(
    val title : String,
    val startTime : String?,
    val endTime : String,
    val failMemo : String
)

class SettingFailFragment : Fragment() {
    private var _binding : FragmentSettingFailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingFailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyFailData = mutableListOf<DummyFailData>()
        repeat(15){
            dummyFailData.add(
                DummyFailData(
                    "title${it+1}",
                    null,
                    "2023-12-06",
                    "asdfasvasdgwqgasasgwqefgasvabqartqgfabvagglakjsvljasivjbiqaegjioqajfvlkajslvjaosigjoiqawjovjasovja"
                )
            )
        }

        val array = SettingFailListAdapter(requireContext(), dummyFailData)
        binding.rvSettingFailMemo.adapter = array
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


class SettingFailListAdapter(
    private val context : Context,
    private val list : List<DummyFailData>
) : BaseAdapter() {

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItem(position: Int): DummyFailData = list[position]

    override fun getCount(): Int = list.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemFailMemoBinding.inflate(LayoutInflater.from(context))
        binding.data = list[position]
        return binding.root
    }

}