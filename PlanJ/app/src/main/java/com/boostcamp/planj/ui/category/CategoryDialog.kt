package com.boostcamp.planj.ui.category

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.AddCategoryBinding

class CategoryDialog(private val context : Context) : Dialog(context) {
    private lateinit var binding : AddCategoryBinding

    init {
        setCanceledOnTouchOutside(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setAddCategory(listener : (Category) -> Unit){
        binding.btAddCategory.setOnClickListener {
            val title = binding.etCategory.text.toString()
            listener(Category(title, title))
            dismiss()
        }
    }

}