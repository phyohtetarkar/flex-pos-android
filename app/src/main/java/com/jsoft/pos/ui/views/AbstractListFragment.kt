package com.jsoft.pos.ui.views

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View

abstract class AbstractListFragment<T> : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
                    val v = rv!!.findChildViewUnder(e!!.x, e.y)
                    val position = rv.getChildAdapterPosition(v)

                    onItemTouch(position)
                    onItemTouch(view, position)
                }

                override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
                    onItemTouch(e!!)
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }

            })

        }

        recyclerView.adapter = _adapter
    }

    @Suppress("UNCHECKED_CAST")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        when (_viewModel) {
            is SimpleListViewModel<T> -> {
                (_viewModel as SimpleListViewModel<T>).list.observe(this, Observer {
                    val adapter = _adapter as SimpleListAdapter<T>
                    adapter.submitList(it)
                    it?.apply {
                        if (isEmpty()) {
                            viewStub.visibility = View.VISIBLE
                        } else {
                            viewStub.visibility = View.GONE
                        }
                    }
                })
            }
            is PagedListViewModel<T> -> {
                (_viewModel as PagedListViewModel<T>).list.observe(this, Observer {
                    val adapter = _adapter as SimplePagedListAdapter<T>
                    adapter.submitList(it)
                    it?.apply {
                        if (isEmpty()) {
                            viewStub.visibility = View.VISIBLE
                        } else {
                            viewStub.visibility = View.GONE
                        }
                    }
                })
            }
        }
    }

    open fun onItemTouch(view: View, position: Int) {
        // optional implementation
    }

    open fun onItemTouch(event: MotionEvent) {
        // optional implementation
    }

    protected abstract val recyclerView: RecyclerView
    protected abstract val viewStub: View
    protected abstract val _viewModel: ListViewModel<T>
    protected abstract val _adapter: RecyclerView.Adapter<BindingViewHolder>
    protected abstract fun onItemTouch(position: Int)

}