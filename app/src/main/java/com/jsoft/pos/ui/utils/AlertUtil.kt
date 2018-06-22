package com.jsoft.pos.ui.utils

import android.content.Context
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.jsoft.pos.R
import kotlinx.android.synthetic.main.layout_toast.view.*
import kotlin.math.roundToInt

class AlertUtil {

    companion object {

        fun showConfirmDelete(context: Context, ok: () -> Unit, cancel: () -> Unit) {
            showConfirm(context, R.string.confirm_message_delete, ok, cancel)
        }

        fun showConfirmDeleteAll(context: Context, ok: () -> Unit, cancel: () -> Unit) {
            showConfirm(context, R.string.confirm_message_delete, ok, cancel)
        }

        fun showToast(context: Context?, msg: String) {
            val toast = Toast(context)
            val layout = LayoutInflater.from(context).inflate(R.layout.layout_toast, null, false)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layout.background = context?.resources?.getDrawable(R.drawable.dark_rounded_back, null)
            } else {
                layout.background = context?.resources?.getDrawable(R.drawable.dark_rounded_back)
            }

            val tv = layout.textViewToast
            tv.text = msg

            val offset = (30 * (context?.resources?.displayMetrics?.density ?: 1f)).roundToInt()

            toast.view = layout
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, offset)
            toast.duration = Toast.LENGTH_LONG
            toast.show()
        }

        private fun showConfirm(context: Context, msgRes: Int, ok: () -> Unit, cancel: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(msgRes)
            builder.setPositiveButton(R.string.delete) { _, _ -> ok() }
            builder.setNegativeButton(R.string.cancel) { _, _ -> cancel() }
            builder.setCancelable(false)
            builder.show()
        }
    }

}