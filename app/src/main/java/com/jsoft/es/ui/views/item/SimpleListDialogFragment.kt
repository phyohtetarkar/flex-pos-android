package com.jsoft.es.ui.views.item

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.jsoft.es.R
import com.jsoft.es.ui.custom.SimpleDividerItemDecoration
import com.jsoft.es.ui.utils.RecyclerViewItemTouchListener
import kotlinx.android.synthetic.main.fragment_simple_list_dialog.*

abstract class SimpleListDialogFragment<T> : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        simpleListRecyclerView.layoutManager = LinearLayoutManager(context)
        simpleListRecyclerView.setHasFixedSize(true)
        simpleListRecyclerView.addItemDecoration(SimpleDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        simpleListRecyclerView.adapter = getAdapter()

        simpleListRecyclerView.addOnItemTouchListener(RecyclerViewItemTouchListener(simpleListRecyclerView, object : RecyclerViewItemTouchListener.OnTouchListener {
            override fun onTouch(view: View, position: Int) {
                onTouch(position)
            }

            override fun onLongTouch(view: View, position: Int) {
            }

        }))

        simpleListDialogCancelButton.setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    protected abstract fun getAdapter(): SimpleListAdapter<T>
    protected abstract fun onTouch(position: Int)

}