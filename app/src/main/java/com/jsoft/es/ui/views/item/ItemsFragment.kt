package com.jsoft.es.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.es.R
import com.jsoft.es.data.model.ItemSearch
import com.jsoft.es.ui.utils.RecyclerViewItemTouchListener
import com.jsoft.es.ui.views.category.CategoryActivity
import com.jsoft.es.ui.views.category.EditCategoryFragment
import kotlinx.android.synthetic.main.fragment_items.*

class ItemsFragment : Fragment() {

    private lateinit var viewModel: ItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemAdapter()

        recyclerViewItems.layoutManager = LinearLayoutManager(context)
        recyclerViewItems.setHasFixedSize(true)
        recyclerViewItems.addOnItemTouchListener(RecyclerViewItemTouchListener(recyclerViewItems, object : RecyclerViewItemTouchListener.OnTouchListener {
            override fun onTouch(view: View, position: Int) {
                val itemVO = adapter.getItemAt(position)
                itemVO?.apply { showEdit((id)) }
            }

            override fun onLongTouch(view: View, position: Int) {
            }

        }))

        recyclerViewItems.adapter = adapter

        fabItems.setOnClickListener { showEdit(0) }

        val stub = viewStubItems.inflate()

        viewModel.items.observe(this, Observer {
            adapter.submitList(it)
            if (it!!.isEmpty()) {
                stub.visibility = View.VISIBLE
            } else {
                stub.visibility = View.GONE
            }
        })

    }

    override fun onResume() {
        super.onResume()
        //viewModel.searchModel.value = ItemSearch()
    }

    private fun showEdit(id: Long) {
        fragmentManager?.beginTransaction()
                ?.replace(R.id.contentItem, EditItemFragment.getInstance(id), ItemActivity.CONTENT)
                ?.addToBackStack(null)
                ?.commit()

    }

    companion object {
        val INSTANCE: ItemsFragment
            get() = ItemsFragment()
    }

}