package com.jsoft.pos.ui.views.lock

import android.os.Bundle
import android.os.Handler
import android.support.v7.preference.PreferenceManager
import android.view.View
import com.jsoft.pos.ui.utils.LockHandler

class AuthorizeLockFragment : LockFragment() {

    private var code: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        code = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("p_lock_code", null)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPinMessage("Enter pin code")
    }

    override fun onPinFilled(pin: String) {
        Handler().postDelayed({
            if (code == pin) {
                LockHandler.backFreeze = false
                dismiss()
            } else {
                onPinError("Wrong pin code")
            }

        }, 400)
    }

}