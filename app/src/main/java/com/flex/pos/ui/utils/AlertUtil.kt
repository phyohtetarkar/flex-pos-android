package com.flex.pos.ui.utils

import android.content.Context
import android.os.Build
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.flex.pos.R
import kotlinx.android.synthetic.main.layout_toast.view.*
import kotlin.math.roundToInt

class AlertUtil {

    companion object {

        fun showConfirmDelete(context: Context, ok: () -> Unit, cancel: () -> Unit) {
            showDialog(context, context.resources?.getString(R.string.confirm_message_delete),
                    R.string.delete,  ok, cancel)
        }

        fun showConfirmDeleteAll(context: Context, ok: () -> Unit, cancel: () -> Unit) {
            showDialog(context, context.resources?.getString(R.string.confirm_message_delete),
                    R.string.delete, ok, cancel)
        }

        fun showToast(context: Context?, stringRes: Int) {
            showToast(context, context?.resources?.getString(stringRes))
        }

        fun showToast(context: Context?, stringRes: Int, vararg args: String) {
            showToast(context, context?.resources?.getString(stringRes, args))
        }

        fun showToast(context: Context?, msg: String?) {
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
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        }

        fun showDialog(context: Context, msg: String?, positiveBtn: Int,  ok: () -> Unit, cancel: (() -> Unit)?) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(msg)
            builder.setPositiveButton(positiveBtn) { _, _ -> ok() }
            cancel?.also { c ->
                builder.setNegativeButton(R.string.cancel) { _, _ -> c() }
            }
            builder.setCancelable(false)
            builder.show()
        }
    }

}