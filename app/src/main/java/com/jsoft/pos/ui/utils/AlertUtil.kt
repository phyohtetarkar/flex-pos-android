package com.jsoft.pos.ui.utils

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_SHORT
import android.support.v7.app.AlertDialog
import android.view.View
import com.jsoft.pos.R

class AlertUtil {

    companion object {
        fun showConfirmDelete(context: Context, ok: () -> Unit, cancel: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.confirm_message_delete)
            builder.setPositiveButton(R.string.confirm_yes) { _, _ -> ok() }
            builder.setNegativeButton(R.string.cancel) { _, _ -> cancel() }
            builder.setCancelable(false)
            builder.show()
        }

        fun showMessage(view: View, idRes: Int) {
            showMessage(view, view.context.resources.getString(idRes))
        }

        fun showMessage(view: View, msg: String) {
            val snackBar = Snackbar.make(view, msg, LENGTH_SHORT)
            snackBar.show()
        }
    }

}