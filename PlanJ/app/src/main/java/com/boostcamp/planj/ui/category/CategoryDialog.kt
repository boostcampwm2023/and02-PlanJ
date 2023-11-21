package com.boostcamp.planj.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.DialogAddCategoryBinding

class CategoryDialog(private val listener: (Category) -> Unit) : DialogFragment() {
    private lateinit var binding: DialogAddCategoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddCategoryBinding.inflate(layoutInflater)
        isCancelable = true
        setStyle(STYLE_NO_TITLE, R.style.dialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDialogCategoryCancel.setOnClickListener {
            dismiss()
        }

        binding.tvDialogCategorySuccess.setOnClickListener {
            val title = binding.tietDialogCategoryInputCategoryName.text.toString()
            listener(Category(title, title))
            dismiss()
        }
    }

}