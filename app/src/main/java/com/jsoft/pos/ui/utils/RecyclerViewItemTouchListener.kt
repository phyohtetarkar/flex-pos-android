package com.jsoft.pos.ui.utils

import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class RecyclerViewItemTouchListener(rv: RecyclerView, private val listener: OnTouchListener?) : RecyclerView.OnItemTouchListener {

    private val detector: GestureDetector

    interface OnTouchListener {

        fun onTouch(view: View, position: Int)

        fun onLongTouch(view: View, position: Int)

    }

    init {

        detector = GestureDetector(rv.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                val view = rv.findChildViewUnder(e.x, e.y)
                if (view != null) {
                    listener?.onLongTouch(view, rv.getChildAdapterPosition(view))
                }
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

        })

    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

        val view = rv.findChildViewUnder(e.x, e.y)

        if (view != null && detector.onTouchEvent(e)) {
            listener?.onTouch(view, rv.getChildAdapterPosition(view))
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

}
