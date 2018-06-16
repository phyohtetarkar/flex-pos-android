package com.jsoft.pos.ui.views.discount

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Item
import com.jsoft.pos.databinding.EditDiscountBinding
import com.jsoft.pos.ui.utils.AlertUtil
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import com.jsoft.pos.ui.views.tax.AssignItemActivity
import kotlinx.android.synthetic.main.activity_edit_discount.*

class EditDiscountActivity : AppCompatActivity() {

    private val ASSIGN_REQ = 1

    private var discountId = 0
    private lateinit var viewModel: EditDiscountViewModel
    private lateinit var binding: EditDiscountBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        discountId = intent.getIntExtra("id", 0)
        viewModel = ViewModelProviders.of(this).get(EditDiscountViewModel::class.java)
        viewModel.assignBtnEnable.value = discountId > 0

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_discount)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_dark)

        viewModel.apply {
            if (discount.value != null) {
                return
            }

            discountInput.value = intent.getIntExtra("id", 0)
        }

        edDiscountName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.assignBtnEnable.value = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        btnAssignDiscount.setOnClickListener {
            val intent = Intent(this, AssignItemActivity::class.java)
            intent.putExtra("id", discountId)
            intent.putExtra("type", Item.AssignType.DISCOUNT)
            viewModel.checkedItemIds?.also {
                intent.putExtra("checked", it.toLongArray())
            }
            startActivityForResult(intent, ASSIGN_REQ)
        }

        btnDeleteDiscount.setOnClickListener {
            AlertUtil.showConfirmDelete(this, {
                viewModel.delete()
            }, {})
        }

        viewModel.deleteSuccess.observe(this, Observer {
            when (it) {
                false -> AlertUtil.showToast(this, resources.getString(R.string.fail_to_delete, "discount"))
                true -> onBackPressed()
            }
        })

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ASSIGN_REQ -> {
                if (resultCode == Activity.RESULT_OK) {

                    data?.getLongArrayExtra("checkedIds")?.apply {
                        viewModel.checkedItemIds = asList()
                    }

                }
            }
        }
    }

}