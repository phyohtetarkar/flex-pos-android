package com.jsoft.es.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.jsoft.es.data.entity.Category
import kotlinx.android.synthetic.main.fragment_simple_list_dialog.*

class DialogCategories : SimpleListDialogFragment<Category>() {

    private lateinit var viewModel: EditItemViewModel
    private lateinit var adapter: SimpleListAdapter<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SimpleListAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity!!.let { ViewModelProviders.of(it).get(EditItemViewModel::class.java) }
        viewModel.categories.observe(this, Observer {
            val list = it?.toMutableList() ?: mutableListOf()
            adapter.setData(list)
            if (list.isEmpty()) {
                viewStubList.inflate()
            }
        })
    }

    override fun getAdapter(): SimpleListAdapter<Category> {
        return adapter
    }

    override fun onTouch(position: Int) {
        viewModel.apply {
            item.get()?.category = adapter.getItemAt(position)
            item.notifyChange()
        }
        dismiss()
    }

}