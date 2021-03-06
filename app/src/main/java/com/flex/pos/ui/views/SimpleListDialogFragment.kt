package com.flex.pos.ui.views

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.flex.pos.R
import kotlinx.android.synthetic.main.fragment_simple_list_dialog.*

abstract class SimpleListDialogFragment<T> : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_simple_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogRecyclerView.apply {
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

        dialogRecyclerView.adapter = getAdapter()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    protected abstract fun getAdapter(): SimpleListAdapter<T>
    protected abstract fun onTouch(position: Int)

}