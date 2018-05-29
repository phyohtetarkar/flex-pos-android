package com.jsoft.pos.ui.views.tax

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.jsoft.pos.R
import com.jsoft.pos.databinding.EditTaxBinding

class EditTaxActivity : AppCompatActivity() {

    private var taxId = 0

    private lateinit var viewModel: EditTaxViewModel
    private lateinit var binding: EditTaxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taxId = intent.getIntExtra("id", 0)
        viewModel = ViewModelProviders.of(this).get(EditTaxViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_tax)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        viewModel.apply {
            if (tax.value != null) {
                return
            }

            taxInput.value = taxId
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

}