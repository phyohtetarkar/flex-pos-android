package com.jsoft.pos.ui.views

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
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

    @Suppress("UNCHECKED_CAST")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (_viewModel is SimpleListViewModel<T>) {

        } else if (_viewModel is PagedListViewModel<T>) {

        }

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

    protected abstract val recyclerView: RecyclerView
    protected abstract val viewStub: View
    protected abstract val _viewModel: ListViewModel<T>
    protected abstract val _adapter: RecyclerView.Adapter<BindingViewHolder>
    protected abstract fun onItemTouch(position: Int)

}