package com.jsoft.pos.ui.custom

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent

class SpinnerEditText : TextInputEditText {

    var onTouchDelegate: (() -> Unit)? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_TAB) {
                return@setOnKeyListener false
            }
            return@setOnKeyListener true
        }

        setOnTouchListener { _, me ->
            if (me.action == MotionEvent.ACTION_DOWN) {
                onTouchDelegate?.invoke()
                requestFocus()
            }
            return@setOnTouchListener true
        }
    }
}

