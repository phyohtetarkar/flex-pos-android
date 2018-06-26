package com.jsoft.pos.ui.views

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.R
import kotlinx.android.synthetic.main.fragment_simple_list.*

abstract class SimpleListFragment<T> : AbstractListFragment<T>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewSimpleList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple, resources.newTheme()))
            } else {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple))
            }
        })

        if (fabSimpleList.visibility == View.VISIBLE) {

            fabSimpleList.setOnClickListener { onItemTouch(-1) }

            recyclerViewSimpleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        if (fabSimpleList.isShown) {
                            fabSimpleList.hide()
                        }
                    } else {
                        if (!fabSimpleList.isShown) {
                            fabSimpleList.show()
                        }
                    }

                }
            })
        }

    }

    override val recyclerView: RecyclerView
        get() = recyclerViewSimpleList

    override val viewStub: View by lazy { viewStubSimpleList.inflate() }

}