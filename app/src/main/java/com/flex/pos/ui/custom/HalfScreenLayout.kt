package com.flex.pos.ui.custom

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet

class HalfScreenLayout : ConstraintLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, (heightMeasureSpec - widthMeasureSpec) + 100)
    }
}