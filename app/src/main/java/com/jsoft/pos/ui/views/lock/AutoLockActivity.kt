package com.jsoft.pos.ui.views.lock

import android.support.v7.app.AppCompatActivity
import com.jsoft.pos.ui.utils.LockHandler

abstract class AutoLockActivity : AppCompatActivity() {

    var navigated = false

    override fun onResume() {
        super.onResume()
        navigated = false
    }

    override fun onPause() {
        super.onPause()
        if (!navigated) {
            LockHandler.handle(this)
        }

    }

    override fun onBackPressed() {
        navigated = true
        super.onBackPressed()
    }

    override fun supportFinishAfterTransition() {
        navigated = true
        super.supportFinishAfterTransition()
    }

}