package com.jsoft.es.ui.views.unit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.es.R
import com.jsoft.es.data.model.UnitSearch
import com.jsoft.es.ui.custom.SimpleDividerItemDecoration
import com.jsoft.es.ui.utils.RecyclerViewItemTouchListener
import com.jsoft.es.ui.utils.SwipeGestureCallback
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

        val adapter = UnitAdapter()

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
                adapter.getItemAt(position).also { viewModel.delete(it) }
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
