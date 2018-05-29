package com.jsoft.pos.ui.views.tax

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.jsoft.pos.data.entity.Tax
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListFragment
import com.jsoft.pos.ui.views.SimpleListViewModel

class TaxesFragment : SimpleListFragment<Tax>() {

    private lateinit var viewModel: TaxesViewModel
    private lateinit var adapter: SimpleListAdapter<Tax>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TaxesViewModel::class.java)

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<Tax>() {
            override fun areItemsTheSame(oldItem: Tax?, newItem: Tax?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Tax?, newItem: Tax?): Boolean {
                return oldItem == newItem
            }

        })
    }

    override val _adapter: SimpleListAdapter<Tax>
        get() = adapter

    override val _viewModel: SimpleListViewModel<Tax>
        get() = viewModel

    override fun onItemTouch(position: Int) {

    }

    override fun showEdit(id: Any) {

    }

    companion object {
        val INSTANCE: TaxesFragment
            get() {
                return TaxesFragment()
            }
    }

}