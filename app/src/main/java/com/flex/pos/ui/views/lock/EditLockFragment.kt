package com.flex.pos.ui.views.lock

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import com.flex.pos.ui.utils.AlertUtil

class EditLockFragment : LockFragment() {

    private var oldCode: String? = null
    private var newCode: String? = null

    private var needToAuthorize = false

    private var confirmPin: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oldCode = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("p_lock_code", null)

        needToAuthorize = (oldCode != null)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (needToAuthorize) {
            setPinMessage("Enter old pin")
        } else {
            setPinMessage("Enter new pin")
        }
    }

    override fun onPinFilled(pin: String) {
        Handler().postDelayed({
            if (needToAuthorize) {
                if (oldCode == pin) {
                    needToAuthorize = false
                    setPinMessage("Enter new pin")
                    resetPin()
                } else {
                    onPinError("Wrong pin code")
                }

            } else if (confirmPin) {
                newCode = pin
                setPinMessage("Confirm pin")
                resetPin()
                confirmPin = false
            } else {
                if (newCode != pin) {
                    onPinError("Pin not match")
                    resetPin()
                } else {
                    setPinMessage("Pin code set")
                    savePinCode()
                }
            }

        }, 400)

    }

    private fun savePinCode() {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.edit().putString("p_lock_code", newCode).apply()
        AlertUtil.showToast(context, "Pin code saved")
        dismiss()
    }

}