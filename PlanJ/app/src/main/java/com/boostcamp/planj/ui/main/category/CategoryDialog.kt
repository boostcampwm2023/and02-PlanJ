package com.boostcamp.planj.ui.main.category

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.data.model.Category
import com.boostcamp.planj.databinding.DialogAddCategoryBinding

enum class CategoryState {
    EMPTY, EXIST, SUCCESS
}

class CategoryDialog(
    private val title: String = "",
    private val listener: (String) -> CategoryState
) :
    DialogFragment() {
    private lateinit var binding: DialogAddCategoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            when (listener(title)) {
                CategoryState.EXIST -> {
                    binding.tietDialogCategoryInputCategoryName.error = "같은 카테고리가 존재합니다."
                    getFocus()
                }

                CategoryState.EMPTY -> {
                    binding.tietDialogCategoryInputCategoryName.error = "값이 비어있습니다."
                    getFocus()
                }

                CategoryState.SUCCESS -> {
                    dismiss()
                }
            }
        }

        binding.tietDialogCategoryInputCategoryName.addTextChangedListener {
            binding.tietDialogCategoryInputCategoryName.error = null
        }

        getFocus()
    }

    private fun getFocus() {
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