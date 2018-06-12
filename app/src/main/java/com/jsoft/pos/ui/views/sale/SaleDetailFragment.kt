package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.databinding.CheckoutBinding
import com.jsoft.pos.ui.utils.SwipeGestureCallback
import com.jsoft.pos.ui.views.SimpleListAdapter
import kotlinx.android.synthetic.main.fragment_sale_detail.*
import kotlinx.android.synthetic.main.layout_checkout_sheet.*

class SaleDetailFragment : Fragment() {

    private var viewModel: CheckoutViewModel? = null
    private lateinit var adapter: SimpleListAdapter<SaleItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let { ViewModelProviders.of(it).get(CheckoutViewModel::class.java) }

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<SaleItem>() {
            override fun areItemsTheSame(oldItem: SaleItem?, newItem: SaleItem?): Boolean {
                return oldItem?.itemId == newItem?.itemId
            }

            override fun areContentsTheSame(oldItem: SaleItem?, newItem: SaleItem?): Boolean {
                return oldItem == newItem
            }

        }, R.layout.layout_sale_item)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = CheckoutBinding.inflate(inflater, container, false)
        binding.checkoutSheet?.setLifecycleOwner(this)
        binding.checkoutSheet?.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCheckout.setOnClickListener {
            fragmentManager?.beginTransaction()
                    ?.replace(R.id.contentCheckout, CompleteCheckoutFragment.INSTANCE)
                    ?.addToBackStack(null)
                    ?.commit()
        }

        val swipeCallback = SwipeGestureCallback(context!!, object : SwipeGestureCallback.OnSwipeDeleteListener {
            override fun onDelete(position: Int) {
                adapter.getItemAt(position).apply {
                    CheckOutItemsHolder.remove(this)
                    if (CheckOutItemsHolder.itemCount == 0) {
                        activity?.onBackPressed()
                    } else {
                        viewModel?.populateSale(CheckOutItemsHolder.list)
                    }
                }
            }

            override fun onCancel(position: Int) {
                adapter.notifyItemChanged(position)
            }
        })

        recyclerViewCheckout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {

                override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {

                    if (swipeCallback.gestureDetector?.onTouchEvent(e) == true) {
                        return true
                    }

                    val v = rv!!.findChildViewUnder(e!!.x, e.y)
                    val position = rv.getChildAdapterPosition(v)

                    if (v != null && e.action == MotionEvent.ACTION_UP) {
                        onItemTouch(position)
                    }

                    return false
                }

            })
        }

        recyclerViewCheckout.adapter = adapter

        val helper = ItemTouchHelper(swipeCallback)
        helper.attachToRecyclerView(recyclerViewCheckout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel?.saleItems?.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel?.populateSale(CheckOutItemsHolder.list)

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_clear, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_clear -> {
                CheckOutItemsHolder.clear()
                activity?.onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onItemTouch(position: Int) {
        val origin = adapter.getItemAt(position)
        val copy = origin.copy()
        copy.item = origin.item
        viewModel?.saleItem?.value = copy

        fragmentManager?.beginTransaction()
                ?.replace(R.id.contentCheckout, EditSaleItemFragment.INSTANCE)
                ?.addToBackStack(null)
                ?.commit()
    }

    companion object {
        val INSTANCE: SaleDetailFragment
            get() {
                return SaleDetailFragment()
            }
    }

}