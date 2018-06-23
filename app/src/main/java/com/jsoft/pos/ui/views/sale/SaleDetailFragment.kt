package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.data.entity.TaxAmount
import com.jsoft.pos.databinding.CheckoutBinding
import com.jsoft.pos.ui.custom.CustomViewAdapter
import com.jsoft.pos.ui.utils.SwipeGestureCallback
import com.jsoft.pos.ui.views.SimpleListAdapter
import kotlinx.android.synthetic.main.fragment_sale_detail.*
import kotlinx.android.synthetic.main.layout_checkout_sheet.*

class SaleDetailFragment : Fragment() {

    private var saleId: Long = 0
    private var viewModel: CheckoutViewModel? = null
    private lateinit var adapter: SimpleListAdapter<SaleItem>
    private var groupTaxAdapter: CustomViewAdapter<TaxAmount>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        saleId = arguments?.getLong("id") ?: 0

        viewModel = activity?.let { ViewModelProviders.of(it).get(CheckoutViewModel::class.java) }

        if (saleId > 0) {
            viewModel?.saleId?.value = saleId
        } else {
            viewModel?.saleItems?.value = CheckOutItemsHolder.list
        }

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<SaleItem>() {
            override fun areItemsTheSame(oldItem: SaleItem?, newItem: SaleItem?): Boolean {
                return oldItem?.itemId == newItem?.itemId
            }

            override fun areContentsTheSame(oldItem: SaleItem?, newItem: SaleItem?): Boolean {
                return oldItem == newItem
            }

        }, R.layout.layout_sale_item)

        groupTaxAdapter = object : CustomViewAdapter<TaxAmount>(itemViewId = R.layout.layout_receipt_tax_sheet) {
            override fun onBindView(holder: SimpleViewHolder, position: Int) {
                holder.bind(list[position])
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = CheckoutBinding.inflate(inflater, container, false)
        binding.checkoutSheet.setLifecycleOwner(this)
        binding.checkoutSheet.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupTaxAdapter?.parent = linearLayoutGroupTaxes

        btnCheckout.setOnClickListener {
            fragmentManager?.beginTransaction()
                    ?.replace(R.id.contentCheckout, CompleteCheckoutFragment.INSTANCE)
                    ?.addToBackStack(null)
                    ?.commit()
        }

        val swipeCallback = SwipeGestureCallback(context!!, object : SwipeGestureCallback.OnSwipeDeleteListener {
            override fun onDelete(position: Int) {
                adapter.getItemAt(position).apply {
                    if (CheckOutItemsHolder.onSale) {
                        CheckOutItemsHolder.remove(this)
                    }

                    viewModel?.removeSaleItem(this)
                    adapter.notifyItemRemoved(position)
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

                    val v = rv!!.findChildViewUnder(e!!.x, e.y)
                    val position = rv.getChildAdapterPosition(v)

                    if (swipeCallback.gestureDetector?.onTouchEvent(e) == true
                            && e.action == MotionEvent.ACTION_UP
                            && v != null) {
                        onItemTouch(position)
                    }

                    return false
                }

            })
        }

        /*recyclerViewCheckout.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple, resources.newTheme()))
            } else {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple))
            }
        })*/

        recyclerViewCheckout.adapter = adapter

        val helper = ItemTouchHelper(swipeCallback)
        helper.attachToRecyclerView(recyclerViewCheckout)

        BottomSheetBehavior.from(checkoutSheet).state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel?.sale?.observe(this, Observer {
            viewModel?.vSaleItems?.value = it?.saleItems
            viewModel?.vTaxAmounts?.value = it?.groupTaxes

        })

        viewModel?.vSaleItems?.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel?.vTaxAmounts?.observe(this, Observer {
            groupTaxAdapter?.submitList(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_sale_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_clear -> {
                CheckOutItemsHolder.clear()
                viewModel?.removeAll()
                adapter.notifyDataSetChanged()
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
        fun getInstance(id: Long): SaleDetailFragment {
            val frag = SaleDetailFragment()
            val b = Bundle().apply {
                putLong("id", id)
            }

            frag.arguments = b
            return frag
        }

    }

}