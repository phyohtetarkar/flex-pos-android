package com.jsoft.pos.ui.utils

import android.content.Context
import android.support.v7.app.AlertDialog
import com.jsoft.pos.R

class AlertUtil {

    companion object {
        fun showConfirmDelete(context: Context, ok: () -> Unit, cancel: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.confirm_message_delete)
            builder.setPositiveButton(R.string.ok) { _, _ -> ok() }
            builder.setNegativeButton(R.string.cancel) { _, _ -> cancel() }
            builder.setCancelable(false)
            builder.show()
        }
    }

}