package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.databinding.CompleteCheckoutBinding
import com.jsoft.pos.ui.views.receipt.ReceiptDetailActivity
import kotlinx.android.synthetic.main.fragment_complete_checkout.*

class CompleteCheckoutFragment : Fragment() {

    private var viewModel: CheckoutViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.let { ViewModelProviders.of(it).get(CheckoutViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = CompleteCheckoutBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCompleteCheckout.setOnClickListener {
            viewModel?.save()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel?.saveObserver?.observe(this, Observer {

            when (it) {
                true -> {
                    val intent = Intent(context, ReceiptDetailActivity::class.java)
                    intent.putExtra("id", viewModel?.sale?.value?.id)
                    startActivity(intent)
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