package com.jsoft.es.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.jsoft.es.data.entity.Unit
import kotlinx.android.synthetic.main.fragment_simple_list_dialog.*

class DialogUnits : SimpleListDialogFragment<Unit>() {

    private lateinit var viewModel: EditItemViewModel
    private lateinit var adapter: SimpleListAdapter<Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = SimpleListAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity!!.let { ViewModelProviders.of(it).get(EditItemViewModel::class.java) }
        viewModel.units.observe(this, Observer {
            val list = it?.toMutableList() ?: mutableListOf()
            adapter.setData(list)
            if (list.isEmpty()) {
                viewStubList.inflate()
            }
        })
    }

    override fun getAdapter(): SimpleListAdapter<Unit> {
        return adapter
    }

    override fun onTouch(position: Int) {
        viewModel.apply {
            item.get()?.unit = adapter.getItemAt(position)
            item.notifyChange()
        }
        dismiss()
    }

}