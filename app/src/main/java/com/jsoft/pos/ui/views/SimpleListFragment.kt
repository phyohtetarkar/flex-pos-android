package com.jsoft.pos.ui.views

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.R
import com.jsoft.pos.ui.custom.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_simple_list.*

abstract class SimpleListFragment<T> : AbstractListFragment<T>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewSimpleList.addItemDecoration(SimpleDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 82))

        fabSimpleList.setOnClickListener { showEdit(0) }

    }

    override val recyclerView: RecyclerView
        get() = recyclerViewSimpleList

    override val viewStub: View by lazy { viewStubSimpleList.inflate() }

    protected abstract fun showEdit(id: Any)

}