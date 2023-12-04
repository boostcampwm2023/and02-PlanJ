package com.boostcamp.planj.ui.main.friendlist

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.boostcamp.planj.R
import com.boostcamp.planj.databinding.DialogAddFriendBinding

class AddFriendDialog : DialogFragment() {

    private var _binding: DialogAddFriendBinding? = null
    private val binding get() = _binding!!

    private var addFriendDialogListener: AddFriendDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawableResource(R.drawable.round_r8_main2)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddFriendBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        binding.tietDialogAddFriendEmail.setText("")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setListener() {
        with(binding) {
            tietDialogAddFriendEmail.addTextChangedListener {
                tilDialogAddFriendLayout.error = null
            }

            tvDialogAddFriendCancel.setOnClickListener {
                dismiss()
            }

            tvDialogAddFriendComplete.setOnClickListener {
                val email = tietDialogAddFriendEmail.text
                if (email != null && email.matches(Regex("""^([A-z0-9_\-.]+)@([A-z0-9_\-]+)\.([a-zA-Z]{2,5})$"""))) {
                    addFriendDialogListener?.onClickComplete(email.toString())
                    dismiss()
                } else {
                    tilDialogAddFriendLayout.error = resources.getString(R.string.email_format_error)
                }
            }
        }
    }

    fun setAddFriendDialogListener(listener: AddFriendDialogListener) {
        addFriendDialogListener = listener
    }
}