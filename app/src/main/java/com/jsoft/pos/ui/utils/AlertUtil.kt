package com.jsoft.pos.ui.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.jsoft.pos.R

class AlertUtil {

    companion object {
        fun showConfirmDelete(context: Context, ok: () -> Unit, cancel: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.confirm_message_delete)
            builder.setPositiveButton(R.string.delete) { _, _ -> ok() }
            builder.setNegativeButton(R.string.cancel) { _, _ -> cancel() }
            builder.setCancelable(false)
            builder.show()
        }

        fun showImageSelectAction(context: Context, handler: (Int) -> Unit) {

        }

        fun showToast(context: Context, msg: String) {
            val toast = Toast(context)
            val layout = LinearLayout(context)
            layout.setPadding(24, 24, 24, 24)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layout.background = context.resources.getDrawable(R.drawable.dark_rounded_back, null)
            } else {
                layout.background = context.resources.getDrawable(R.drawable.dark_rounded_back)
            }

            val tv = TextView(context)
            tv.textSize = 18f
            tv.text = msg
            tv.setTextColor(Color.WHITE)

            layout.addView(tv)

            toast.view = layout
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        }
    }

}