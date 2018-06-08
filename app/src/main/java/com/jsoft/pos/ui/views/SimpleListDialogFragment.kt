package com.jsoft.pos.ui.views

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.R
import kotlinx.android.synthetic.main.fragment_simple_list_dialog.*

abstract class SimpleListDialogFragment<T> : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        simpleListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener(){

                override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
                    val v = rv?.findChildViewUnder(e!!.x, e.y)
                    rv?.getChildAdapterPosition(v)?.apply {
                        onTouch(this)
                    }

                    return false
                }

            })
        }

        simpleListRecyclerView.adapter = getAdapter()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    protected abstract fun getAdapter(): SimpleListAdapter<T>
    protected abstract fun onTouch(position: Int)

}