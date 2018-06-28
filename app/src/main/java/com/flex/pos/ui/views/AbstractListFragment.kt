package com.flex.pos.ui.views

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.flex.pos.ui.utils.RecyclerViewItemTouchListener

abstract class AbstractListFragment<T> : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addOnItemTouchListener(getItemTouchListener(context, recyclerView))

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

    open fun getItemTouchListener(context: Context, recyclerView: RecyclerView): RecyclerView.OnItemTouchListener {
        return RecyclerViewItemTouchListener(context, recyclerView, object : RecyclerViewItemTouchListener.OnTouchListener {
            override fun onTouch(view: View, position: Int) {
                onItemTouch(position)
                onItemTouch(view, position)
            }

            override fun onLongTouch(view: View, position: Int) {
            }

        })
    }

    open fun onItemTouch(view: View, position: Int) {
        // optional implementation
    }

    protected abstract val recyclerView: RecyclerView
    protected abstract val viewStub: View
    protected abstract val _viewModel: ListViewModel<T>
    protected abstract val _adapter: RecyclerView.Adapter<BindingViewHolder>
    protected abstract fun onItemTouch(position: Int)

}