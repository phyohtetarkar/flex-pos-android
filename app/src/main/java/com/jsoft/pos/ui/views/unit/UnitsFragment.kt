package com.jsoft.pos.ui.views.unit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.UnitSearch
import com.jsoft.pos.ui.utils.AlertUtil
import com.jsoft.pos.ui.utils.SwipeGestureCallback
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListFragment
import com.jsoft.pos.ui.views.SimpleListViewModel
import kotlinx.android.synthetic.main.fragment_simple_list.*

class UnitsFragment : SimpleListFragment<Unit>() {

    private lateinit var adapter: SimpleListAdapter<Unit>
    private lateinit var viewModel: UnitsViewModel
    private var swipeCallback: SwipeGestureCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(UnitsViewModel::class.java)

        adapter = UnitAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeCallback = SwipeGestureCallback(context!!, object : SwipeGestureCallback.OnSwipeDeleteListener {
            override fun onDelete(position: Int) {
                adapter.getItemAt(position).apply { viewModel.delete(this) }
            }

            override fun onCancel(position: Int) {
                adapter.notifyItemChanged(position)
            }
        })

        val helper = ItemTouchHelper(swipeCallback)
        helper.attachToRecyclerView(recyclerViewSimpleList)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.deleteSuccess.observe(this, Observer {
            if (it == false) {
                AlertUtil.showToast(activity!!, R.string.fail_to_delete, "unit")
            }
        })
    }

    override fun getItemTouchListener(context: Context, recyclerView: RecyclerView): RecyclerView.OnItemTouchListener {
        return object : RecyclerView.SimpleOnItemTouchListener() {

            override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {

                val v = rv!!.findChildViewUnder(e!!.x, e.y)
                val position = rv.getChildAdapterPosition(v)

                if (swipeCallback?.gestureDetector?.onTouchEvent(e) == true
                        && e.action == MotionEvent.ACTION_UP
                        && v != null) {
                    onItemTouch(position)
                }

                return false
            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = UnitSearch()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.setTitle(R.string.units)
    }

    override val _adapter: SimpleListAdapter<Unit>
        get() = adapter

    override val _viewModel: SimpleListViewModel<Unit>
        get() = viewModel

    override fun onItemTouch(position: Int) {
        var id = 0

        if (position > -1) {
            id = adapter.getItemAt(position).id
        }

        val ft = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
        val frag = EditUnitFragment.getInstance(id as Int)
        frag.show(ft, "dialog")
    }

}
