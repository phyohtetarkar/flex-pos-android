package com.jsoft.es.ui.views.unit

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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

    enum class Mode {
        SHOW, SELECT
    }

    private lateinit var mode: Mode

    private lateinit var viewModel: UnitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getSerializable("mode")?.apply { mode = this as Mode }

        viewModel = ViewModelProviders.of(this).get(UnitsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_units, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UnitAdapter()
        recyclerViewUnits.layoutManager = LinearLayoutManager(context)
        recyclerViewUnits.setHasFixedSize(true)
        recyclerViewUnits.addItemDecoration(SimpleDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerViewUnits.addOnItemTouchListener(RecyclerViewItemTouchListener(recyclerViewUnits, object : RecyclerViewItemTouchListener.OnTouchListener {
            override fun onTouch(view: View, position: Int) {
                val (id) = adapter.getItemAt(position)
                when (mode) {
                    Mode.SHOW -> showEdit(id)
                    Mode.SELECT -> {
                        val i = Intent()
                        i.putExtra("unitId", id)
                        activity?.setResult(Activity.RESULT_OK, i)
                        activity?.onBackPressed()
                    }
                }

            }

            override fun onLongTouch(view: View, position: Int) {}
        }))

        recyclerViewUnits.adapter = adapter

        val callback = SwipeGestureCallback(object : SwipeGestureCallback.OnSwipeDeleteListener {
            override fun onDelete(position: Int) {
                adapter.getItemAt(position).also { viewModel.delete(it) }
            }

            override fun onCancel(position: Int) {
                adapter.refreshItemAt(position)
            }
        })
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recyclerViewUnits)

        fabUnits.setOnClickListener { showEdit(0) }

        val stub = viewStubUnits.inflate()

        viewModel.units.observe(this, Observer {
            val list = it?.toMutableList() ?: mutableListOf()
            adapter.setData(list)
            if (list.isEmpty()) {
                stub.visibility = View.VISIBLE
            } else {
                stub.visibility = View.GONE
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
        fun getInstance(mode: Mode): UnitsFragment {
            val frag = UnitsFragment()

            val args = Bundle()
            args.putSerializable("mode", mode)
            frag.arguments = args

            return frag
        }
    }

}
