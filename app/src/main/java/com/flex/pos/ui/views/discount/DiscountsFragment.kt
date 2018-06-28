package com.flex.pos.ui.views.discount

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import com.flex.pos.MainActivity
import com.flex.pos.R
import com.flex.pos.data.entity.Discount
import com.flex.pos.ui.views.BindingViewHolder
import com.flex.pos.ui.views.ListViewModel
import com.flex.pos.ui.views.SimpleListAdapter
import com.flex.pos.ui.views.SimpleListFragment
import com.flex.pos.ui.views.lock.AutoLockActivity

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.setTitle(R.string.discounts)
    }

    override val _viewModel: ListViewModel<Discount>
        get() = viewModel

    override val _adapter: RecyclerView.Adapter<BindingViewHolder>
        get() = adapter

    override fun onItemTouch(position: Int) {
        var id = 0

        if (position > -1) {
            id = adapter.getItemAt(position).id
        }

        (activity as? AutoLockActivity)?.navigated = true

        val i = Intent(context, EditDiscountActivity::class.java)
        i.putExtra("id", id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }
    }

}