package com.boostcamp.planj.ui.category

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.AddCategoryBinding

class CategoryDialog() : DialogFragment() {
    private lateinit var binding : AddCategoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddCategoryBinding.inflate(layoutInflater)
        isCancelable = true
        setStyle(STYLE_NO_TITLE, R.style.dialog)
        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddCategoryBinding.inflate(layoutInflater)
        return binding.root
    }
    fun setAddCategory(listener : (Category) -> Unit){
        binding.btAddCategory.setOnClickListener {
            val title = binding.etCategory.text.toString()
            listener(Category(title, title))
            dismiss()
        }
    }

}