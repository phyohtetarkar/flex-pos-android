package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.databinding.EditSaleItemBinding
import kotlinx.android.synthetic.main.fragment_edit_sale_item.*

class EditSaleItemFragment : Fragment() {

    private var viewModel: CheckoutViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.let { ViewModelProviders.of(it).get(CheckoutViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = EditSaleItemBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnUpdateSaleItem.setOnClickListener {
            if (CheckOutItemsHolder.onSale) {
                CheckOutItemsHolder.update(viewModel?.saleItem?.value)
            }
            viewModel?.updateSaleItem()
            activity?.onBackPressed()
        }

        btnAddQty.setOnClickListener {
            viewModel?.saleItem?.value?.also {
                edSaleItemQty.setText(it.quantity.plus(1).toString())
            }
        }

        btnSubQty.setOnClickListener {
            viewModel?.saleItem?.value?.also {
                if (it.quantity > 1) {
                    edSaleItemQty.setText(it.quantity.minus(1).toString())
                }
            }
        }

    }

    companion object {
        val INSTANCE: EditSaleItemFragment
            get() {
                return EditSaleItemFragment()
            }
    }

}