package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
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
            viewModel?.updateSaleItem()
            activity?.onBackPressed()
        }

        viewModel?.saleItem?.observe(this, Observer {
            edSaleItemAmount.setText(it?.quantity.toString())
        })

        btnAddQty.setOnClickListener {
            viewModel?.saleItem?.value?.apply {
                edSaleItemAmount.setText(quantity.inc().toString())
            }
        }

        btnSubQty.setOnClickListener {
            viewModel?.saleItem?.value?.apply {
                if (quantity > 1) {
                    edSaleItemAmount.setText(quantity.dec().toString())
                }
            }
        }

        edSaleItemAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                viewModel?.saleItem?.value?.also {
                    if (!s.isNullOrEmpty() && s.toString().toInt() > 0) {
                        it.quantity = s.toString().toInt()
                    } else {
                        it.quantity = 1
                    }
                    viewModel?.totalSaleItemPrice?.value = it.total
                }

            }

        })


    }

    companion object {
        val INSTANCE: EditSaleItemFragment
            get() {
                return EditSaleItemFragment()
            }
    }

}