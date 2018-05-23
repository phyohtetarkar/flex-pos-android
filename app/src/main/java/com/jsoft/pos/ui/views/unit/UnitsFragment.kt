package com.jsoft.pos.ui.views.unit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.UnitSearch
import com.jsoft.pos.ui.custom.SimpleDividerItemDecoration
import com.jsoft.pos.ui.utils.RecyclerViewItemTouchListener
import com.jsoft.pos.ui.utils.SwipeGestureCallback
import com.jsoft.pos.ui.views.SimpleListAdapter
import kotlinx.android.synthetic.main.fragment_units.*

class UnitsFragment : Fragment() {

    private lateinit var viewModel: UnitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(UnitsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_units, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<Unit>() {
            override fun areItemsTheSame(oldItem: Unit, newItem: Unit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Unit, newItem: Unit): Boolean {
                return oldItem == newItem
            }
        })

        recyclerViewUnits.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(SimpleDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnItemTouchListener(RecyclerViewItemTouchListener(this, object : RecyclerViewItemTouchListener.OnTouchListener {
                override fun onTouch(view: View, position: Int) {
                    val (id) = adapter.getItemAt(position)
                    showEdit(id)
                }

                override fun onLongTouch(view: View, position: Int) {

                }
            }))

            this.adapter = adapter
        }

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

        fabUnits.setOnClickListener { showEdit(0) }

        val stub = viewStubUnits.inflate()

        viewModel.units.observe(this, Observer {
            adapter.submitList(it)
            it?.apply {
                if (isEmpty()) {
                    stub.visibility = View.VISIBLE
                } else {
                    stub.visibility = View.GONE
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = UnitSearch()
    }

    private fun showEdit(id: Int) {
        val ft = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
        ft?.addToBackStack(null)

        val frag = EditUnitFragment.getInstance(id)
        frag.show(ft, "dialog")
    }

    companion object {
        val INSTANCE: UnitsFragment
            get() {
                return UnitsFragment()
            }
    }

}
