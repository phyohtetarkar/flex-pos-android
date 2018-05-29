package com.jsoft.pos.ui.views.discount

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.ui.views.BindingViewHolder
import com.jsoft.pos.ui.views.ListViewModel
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListFragment

class DiscountsFragment : SimpleListFragment<Discount>() {

    private lateinit var viewModel: DiscountsViewModel
    private lateinit var adapter: SimpleListAdapter<Discount>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DiscountsViewModel::class.java)
        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<Discount>() {
            override fun areItemsTheSame(oldItem: Discount?, newItem: Discount?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Discount?, newItem: Discount?): Boolean {
                return oldItem == newItem
            }

        }, R.layout.layout_simple_list_item_2)
    }

    override val _viewModel: ListViewModel<Discount>
        get() = viewModel

    override val _adapter: RecyclerView.Adapter<BindingViewHolder>
        get() = adapter

    override fun onItemTouch(position: Int) {
    }

    override fun showEdit(id: Any) {
    }

    companion object {
        val INSTANCE: DiscountsFragment
            get() {
                return DiscountsFragment()
            }
    }

}