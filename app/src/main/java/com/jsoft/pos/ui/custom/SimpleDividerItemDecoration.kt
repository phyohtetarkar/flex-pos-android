package com.jsoft.pos.ui.custom

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View

class SimpleDividerItemDecoration(context: Context?, orientation: Int) : DividerItemDecoration(context, orientation) {

    private var leftOffset = 0

    constructor(context: Context?, orientation: Int, leftOffset: Int) : this(context, orientation) {
        this.leftOffset = leftOffset
    }

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(0, 0, 0, 1)
    }

}
