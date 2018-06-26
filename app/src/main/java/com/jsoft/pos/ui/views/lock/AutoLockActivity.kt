package com.jsoft.pos.ui.views.lock

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jsoft.pos.ui.utils.LockHandler

abstract class AutoLockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LockHandler.navigated(this, false)
    }

    override fun onPause() {
        super.onPause()
        LockHandler.handle(this)
    }

    override fun onBackPressed() {
        LockHandler.navigated(this, true)
        super.onBackPressed()
    }

}