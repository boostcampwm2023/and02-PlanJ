package com.boostcamp.planj.ui.category

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.DialogAddCategoryBinding

class CategoryDialog(private val title: String = "", private val listener: (Category) -> Unit) :
    DialogFragment() {
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
        binding.categoryname = title
        binding.lifecycleOwner = viewLifecycleOwner
        binding.tietDialogCategoryInputCategoryName.setText(title)
        binding.tvDialogCategoryCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDialogCategorySuccess.setOnClickListener {
            val title = binding.tietDialogCategoryInputCategoryName.text.toString()
            listener(Category(title, title))
            dismiss()
        }

        binding.tietDialogCategoryInputCategoryName.requestFocus()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.tietDialogCategoryInputCategoryName.windowInsetsController?.show(
                WindowInsetsCompat.Type.ime()
            )
        } else {
            activity?.let {
                WindowInsetsControllerCompat(it.window, binding.tietDialogCategoryInputCategoryName)
                    .show(WindowInsetsCompat.Type.ime())
            }
        }
    }

}