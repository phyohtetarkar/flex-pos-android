package com.jsoft.pos.ui.views.unit

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.UnitSearch
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

    override fun getItemTouchListener(context: Context, recyclerView: RecyclerView): RecyclerView.OnItemTouchListener {
        return object : RecyclerView.SimpleOnItemTouchListener() {

            override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {

                if (swipeCallback?.gestureDetector?.onTouchEvent(e) == true) {
                    return true
                }

                val v = rv!!.findChildViewUnder(e!!.x, e.y)
                val position = rv.getChildAdapterPosition(v)

                if (v != null && e.action == MotionEvent.ACTION_UP) {
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
        showEdit(adapter.getItemAt(position).id)
    }

    override fun showEdit(id: Any) {
        val ft = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
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
