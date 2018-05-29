package com.jsoft.pos.ui.views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jsoft.pos.ui.utils.RecyclerViewItemTouchListener

abstract class AbstractListFragment<T> : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addOnItemTouchListener(RecyclerViewItemTouchListener(this, object : RecyclerViewItemTouchListener.OnTouchListener {
                override fun onTouch(view: View, position: Int) {
                    onItemTouch(position)
                }

                override fun onLongTouch(view: View, position: Int) {

                }
            }))
        }

        recyclerView.adapter = _adapter
    }

    protected abstract val recyclerView: RecyclerView
    protected abstract val viewStub: View
    protected abstract val _adapter: RecyclerView.Adapter<BindingViewHolder>
    protected abstract fun onItemTouch(position: Int)

}