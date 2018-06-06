package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.databinding.CheckoutBinding
import com.jsoft.pos.ui.views.SimpleListAdapter
import kotlinx.android.synthetic.main.fragment_sale_detail.*

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = CheckoutBinding.inflate(inflater, container, false)
        binding.checkoutSheet?.setLifecycleOwner(this)
        binding.checkoutSheet?.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewCheckout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        recyclerViewCheckout.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel?.saleItems?.observe(this, Observer {
            adapter.submitList(it)
        })

        activity?.intent?.getLongArrayExtra("itemIds")?.apply {
            viewModel?.createFromItemIds(this)
        }
    }

    companion object {
        val INSTANCE: SaleDetailFragment
            get() {
                return SaleDetailFragment()
            }
    }

}