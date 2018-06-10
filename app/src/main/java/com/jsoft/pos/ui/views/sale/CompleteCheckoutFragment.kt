package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.databinding.CompleteCheckoutBinding
import kotlinx.android.synthetic.main.fragment_complete_checkout.*

class CompleteCheckoutFragment : Fragment() {

    private var viewModel: CheckoutViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.let { ViewModelProviders.of(it).get(CheckoutViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = CompleteCheckoutBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edPayPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                viewModel?.sale?.value?.also {
                    if (!s.isNullOrEmpty() && s.toString().toDouble() > 0) {
                        it.payPrice = s.toString().toDouble()
                        it.change = it.payPrice - it.totalPrice
                    } else {
                        it.payPrice = 0.0
                    }
                    viewModel?.change?.value = it.change
                }

            }

        })

    }

    companion object {
        val INSTANCE: CompleteCheckoutFragment
            get() {
                return CompleteCheckoutFragment()
            }
    }

}