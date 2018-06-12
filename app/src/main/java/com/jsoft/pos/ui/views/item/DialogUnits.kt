package com.jsoft.pos.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListDialogFragment
import kotlinx.android.synthetic.main.fragment_simple_list_dialog.*

class DialogUnits : SimpleListDialogFragment<Unit>() {

    private lateinit var viewModel: EditItemViewModel
    private lateinit var adapter: SimpleListAdapter<Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<Unit>() {
            override fun areItemsTheSame(oldItem: Unit, newItem: Unit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Unit, newItem: Unit): Boolean {
                return oldItem == newItem
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity!!.let { ViewModelProviders.of(it).get(EditItemViewModel::class.java) }
        viewModel.units.observe(this, Observer {
            adapter.submitList(it)
            it?.apply {
                if (isEmpty()) {
                    viewStubList.inflate()
                }
            }
        })
    }

    override fun getAdapter(): SimpleListAdapter<Unit> {
        return adapter
    }

    override fun onTouch(position: Int) {
        viewModel.apply {
            item.value?.unit = adapter.getItemAt(position)
        }
        dismiss()
    }

}