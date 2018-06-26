package com.jsoft.pos.ui.utils

import android.support.v4.app.FragmentActivity
import android.support.v7.preference.PreferenceManager
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.ui.views.lock.AuthorizeLockFragment

object LockHandler {

    var backFreeze = false

    fun navigated(activity: FragmentActivity?, flag: Boolean) {
        val app = activity?.application as? FlexPosApplication
        app?.isNavigation = flag
    }

    fun handle(activity: FragmentActivity?) {
        val app = activity?.application as? FlexPosApplication

        val isEnable = PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean("p_app_lock", false)

        if (isEnable) {
            backFreeze = true
            val ft = activity?.supportFragmentManager?.beginTransaction()
            val prev = activity?.supportFragmentManager?.findFragmentByTag("lock")
            if (prev != null) {
                ft?.remove(prev)
            }
            val frag = ServiceLocator.locate(AuthorizeLockFragment::class.java)
            frag?.show(ft, "lock")
        }

    }

}