package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jsoft.pos.R
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import com.jsoft.pos.ui.utils.LockHandler
import java.util.concurrent.locks.Lock

class CheckoutActivity : AppCompatActivity() {

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

        LockHandler.navigated(this, false)

    }

    override fun onBackPressed() {
        LockHandler.navigated(this, true)
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}