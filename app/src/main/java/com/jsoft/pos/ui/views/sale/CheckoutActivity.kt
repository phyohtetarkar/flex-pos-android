package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import com.jsoft.pos.R
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import com.jsoft.pos.ui.views.lock.AutoLockActivity

class CheckoutActivity : AutoLockActivity() {

    private lateinit var viewModel: CheckoutViewModel

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        viewModel = ViewModelProviders.of(this).get(CheckoutViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_dark)

        supportFragmentManager.beginTransaction()
                .replace(R.id.contentCheckout, SaleDetailFragment.getInstance(intent.getLongExtra("id", 0)))
                .commit()


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}