package com.kstd.android.jth.ui.feature.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.kstd.android.jth.R
import androidx.core.graphics.drawable.toDrawable

class ProgressDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): ProgressDialogFragment {
            return ProgressDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.setContentView(R.layout.dialog_progress)
        isCancelable = false
        return dialog
    }
}