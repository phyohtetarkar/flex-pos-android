package com.jsoft.pos.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.view.View
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListDialogFragment
import com.jsoft.pos.ui.views.unit.UnitAdapter
import kotlinx.android.synthetic.main.fragment_simple_list_dialog.*

class DialogUnits : SimpleListDialogFragment<Unit>() {

    private var viewModel: EditItemViewModel? = null
    private lateinit var adapter: SimpleListAdapter<Unit>

    private var stub: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.let { ViewModelProviders.of(it).get(EditItemViewModel::class.java) }

        adapter = UnitAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel?.units?.observe(activity!!, Observer {
            adapter.submitList(it)
            it?.apply {
                if (isEmpty()) {
                    if (stub == null) {
                        stub = viewStubList.inflate()
                    } else {
                        stub?.visibility = View.VISIBLE
                    }
                } else {
                    stub?.visibility = View.GONE
                }
            }

        })
    }

    override fun getAdapter(): SimpleListAdapter<Unit> {
        return adapter
    }

    override fun onTouch(position: Int) {
        viewModel?.apply {
            item.value?.unit = adapter.getItemAt(position)
        }
        dismiss()
    }

}