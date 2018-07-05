package com.flex.pos.ui

import android.os.Bundle

import com.flex.pos.R
import com.flex.pos.ui.views.lock.AutoLockActivity

class AboutActivity : AutoLockActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }
}
