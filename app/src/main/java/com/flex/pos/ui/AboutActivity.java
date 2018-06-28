package com.flex.pos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.flex.pos.R;
import com.flex.pos.ui.views.lock.AutoLockActivity;

public class AboutActivity extends AutoLockActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
