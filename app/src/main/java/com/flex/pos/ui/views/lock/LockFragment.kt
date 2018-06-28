package com.flex.pos.ui.views.lock

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.flex.pos.R
import kotlinx.android.synthetic.main.layout_pin_lock.*

abstract class LockFragment : DialogFragment() {

    private val pinCode = StringBuilder()
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        } else {
            setStyle(STYLE_NORMAL, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        }

        vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_pin_lock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 0 until constLayoutLockNumbers.childCount) {
            constLayoutLockNumbers.getChildAt(i).setOnClickListener { onClick(it) }
        }

    }

    private fun onClick(view: View) {
        val btn = view as Button

        if (btn.id == R.id.btnClear) {
            resetPin()
        } else {
            activatePin(btn.text.toString())
        }
    }

    private fun activatePin(s: String) {
        if (pinCode.length == 4) {
            return
        }

        pinCode.append(s)
        val i = pinCode.length
        if (i == 4) {
            linearLayoutPins.getChildAt(i - 1).isSelected = true
            onPinFilled(pinCode.toString())
        } else {
            linearLayoutPins.getChildAt(i - 1).isSelected = true
        }
    }

    protected fun resetPin() {
        pinCode.delete(0, 4)
        for (i in 0 until linearLayoutPins.childCount) {
            linearLayoutPins.getChildAt(i).isSelected = false
        }
    }

    protected fun onPinError(message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvPinMessage.setTextColor(resources.getColor(R.color.colorError, null))
        } else {
            tvPinMessage.setTextColor(resources.getColor(R.color.colorError))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(200)
        }
        tvPinMessage.text = message
        resetPin()
    }

    protected fun setPinMessage(message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvPinMessage.setTextColor(resources.getColor(R.color.colorTextPrimary, null))
        } else {
            tvPinMessage.setTextColor(resources.getColor(R.color.colorTextPrimary))
        }
        tvPinMessage.text = message
    }

    protected abstract fun onPinFilled(pin: String)

}