package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jsoft.pos.R

class CheckoutActivity : AppCompatActivity() {

    private lateinit var viewModel: CheckoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        viewModel = ViewModelProviders.of(this).get(CheckoutViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.getLongArrayExtra("itemIds")?.apply {
            viewModel.createFromItemIds(this)
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.checkout_main, SaleDetailFragment.INSTANCE)
                .commit()

    }

}