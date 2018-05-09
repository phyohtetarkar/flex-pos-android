package com.jsoft.es.ui.views.item

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.jsoft.es.data.entity.Category

class SelectCategoryFragment : SimpleListDialogFragment<Category>() {

    private lateinit var viewModel: EditItemViewModel
    private lateinit var adapter: SimpleListAdapter<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SimpleListAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(EditItemViewModel::class.java)
    }

    override fun getAdapter(): SimpleListAdapter<Category> {
        return adapter
    }

    override fun onTouch(position: Int) {
        viewModel.item.get()?.category = adapter.getItemAt(position)
        dismiss()
    }

}