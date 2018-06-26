package com.jsoft.pos.ui.views.tax

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Tax
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListFragment
import com.jsoft.pos.ui.views.SimpleListViewModel
import com.jsoft.pos.ui.views.lock.AutoLockActivity

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

        }, R.layout.layout_simple_list_item_2)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.setTitle(R.string.taxes)
    }

    override val _adapter: SimpleListAdapter<Tax>
        get() = adapter

    override val _viewModel: SimpleListViewModel<Tax>
        get() = viewModel

    override fun onItemTouch(position: Int) {
        var id = 0

        if (position > -1) {
            id = adapter.getItemAt(position).id
        }

        (activity as? AutoLockActivity)?.navigated = true

        val i = Intent(context, EditTaxActivity::class.java)
        i.putExtra("id", id as Int)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }
    }

}