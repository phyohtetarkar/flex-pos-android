package com.jsoft.pos.ui.custom

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.View
import com.jsoft.pos.R

open class CustomEditText : AppCompatEditText {

    private val STATE_ERROR = IntArray(1) { R.attr.state_error }

    var hasError = false
        set(value) {
            field = value
            if (value) {
                background.state = STATE_ERROR
            }
        }

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (hasError) {
            background.state = View.FOCUSED_STATE_SET
            hasError = false
        }
    }

}

