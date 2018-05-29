package com.jsoft.pos.ui.views.discount

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
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

}