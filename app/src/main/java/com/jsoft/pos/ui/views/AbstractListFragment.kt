package com.jsoft.pos.ui.views

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewStub
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

        _viewModel.list.observe(this, Observer {
            _adapter.submitList(it)
            it?.apply {
                if (isEmpty()) {
                    viewStub.visibility = View.VISIBLE
                } else {
                    viewStub.visibility = View.GONE
                }
            }
        })

    }

    abstract val recyclerView: RecyclerView
    abstract val viewStub: View
    abstract val _adapter: SimpleListAdapter<T>
    abstract val _viewModel: ListViewModel<T>
    abstract fun onItemTouch(position: Int)

}