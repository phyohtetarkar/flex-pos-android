package com.jsoft.pos.ui.views.unit

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.UnitSearch
import com.jsoft.pos.ui.utils.SwipeGestureCallback
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListFragment
import com.jsoft.pos.ui.views.SimpleListViewModel
import kotlinx.android.synthetic.main.fragment_units.*

class UnitsFragment : SimpleListFragment<Unit>() {

    private lateinit var adapter: SimpleListAdapter<Unit>
    private lateinit var viewModel: UnitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(UnitsViewModel::class.java)

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<Unit>() {
            override fun areItemsTheSame(oldItem: Unit, newItem: Unit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Unit, newItem: Unit): Boolean {
                return oldItem == newItem
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = SwipeGestureCallback(object : SwipeGestureCallback.OnSwipeDeleteListener {
            override fun onDelete(position: Int) {
                adapter.getItemAt(position).apply { viewModel.delete(this) }
            }

            override fun onCancel(position: Int) {
                adapter.notifyItemChanged(position)
            }
        })
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recyclerViewUnits)

    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = UnitSearch()
    }

    override val _adapter: SimpleListAdapter<Unit>
        get() = adapter

    override val _viewModel: SimpleListViewModel<Unit>
        get() = viewModel

    override fun onItemTouch(position: Int) {
        showEdit(adapter.getItemAt(position).id)
    }

    override fun showEdit(id: Any) {
        val ft = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
        ft?.addToBackStack(null)

        val frag = EditUnitFragment.getInstance(id as Int)
        frag.show(ft, "dialog")
    }

    companion object {
        val INSTANCE: UnitsFragment
            get() {
                return UnitsFragment()
            }
    }

}
