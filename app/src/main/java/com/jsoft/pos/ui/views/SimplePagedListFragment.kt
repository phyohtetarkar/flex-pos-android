package com.jsoft.pos.ui.views

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View

abstract class SimplePagedListFragment<T> : AbstractListFragment<T>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

    protected abstract val _viewModel: PagedListViewModel<T>
    abstract override val _adapter: SimplePagedListAdapter<T>

}