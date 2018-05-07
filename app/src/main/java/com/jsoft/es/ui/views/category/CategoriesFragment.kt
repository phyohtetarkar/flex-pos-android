package com.jsoft.es.ui.views.category

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.es.R
import com.jsoft.es.data.model.CategorySearch
import com.jsoft.es.ui.custom.SimpleDividerItemDecoration
import com.jsoft.es.ui.utils.RecyclerViewItemTouchListener
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {
    private lateinit var viewModel: CategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CategoryAdapter()

        recyclerViewCategories.layoutManager = LinearLayoutManager(activity)
        recyclerViewCategories.setHasFixedSize(true)
        recyclerViewCategories.addItemDecoration(SimpleDividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recyclerViewCategories.addOnItemTouchListener(RecyclerViewItemTouchListener(recyclerViewCategories, object : RecyclerViewItemTouchListener.OnTouchListener {
            override fun onTouch(view: View, position: Int) {
                val (id) = adapter.getItemAt(position)
                showEdit(id)

            }

            override fun onLongTouch(view: View, position: Int) {

            }
        }))

        recyclerViewCategories.adapter = adapter

        fabCategories.setOnClickListener { showEdit(0) }

        val stub = viewStubCategories.inflate()

        viewModel.categories.observe(this, Observer {
            val list = it?.toMutableList() ?: mutableListOf()
            adapter.setData(list)
            if (list.isEmpty()) {
                stub.visibility = View.VISIBLE
            } else {
                stub.visibility = View.GONE
            }

        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = CategorySearch()
    }

    private fun showEdit(id: Int) {
        fragmentManager?.beginTransaction()
                ?.replace(R.id.contentCategory, EditCategoryFragment.getInstance(id), CategoryActivity.CONTENT)
                ?.addToBackStack(null)
                ?.commit()

    }

    companion object {
        val INSTANCE: CategoriesFragment
            get() {
                return CategoriesFragment()
            }
    }

}
