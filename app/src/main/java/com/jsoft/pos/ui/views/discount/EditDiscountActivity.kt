package com.jsoft.pos.ui.views.discount

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.jsoft.pos.R
import com.jsoft.pos.databinding.EditDiscountBinding

class EditDiscountActivity : AppCompatActivity() {

    private var discountId = 0
    private lateinit var viewModel: EditDiscountViewModel
    private lateinit var binding: EditDiscountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        discountId = intent.getIntExtra("id", 0)
        viewModel = ViewModelProviders.of(this).get(EditDiscountViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_discount)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_dark)

        viewModel.apply {
            if (discount.value != null) {
                return
            }

            discountInput.value = discountId
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_save -> {
                viewModel.save()
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}