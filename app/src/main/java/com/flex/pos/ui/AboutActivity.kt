package com.flex.pos.ui

import android.os.Bundle
import com.flex.pos.BuildConfig

import com.flex.pos.R
import com.flex.pos.ui.views.lock.AutoLockActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AutoLockActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        tvAppVersion.text = resources.getString(R.string.app_version, BuildConfig.VERSION_NAME)
    }
}
