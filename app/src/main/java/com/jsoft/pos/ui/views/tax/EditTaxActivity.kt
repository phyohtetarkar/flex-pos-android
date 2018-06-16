package com.jsoft.pos.ui.views.tax

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
import com.jsoft.pos.databinding.EditTaxBinding
import com.jsoft.pos.ui.utils.AlertUtil
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import kotlinx.android.synthetic.main.activity_edit_tax.*

class EditTaxActivity : AppCompatActivity() {

    private val ASSIGN_REQ = 1

    private var taxId = 0

    private lateinit var viewModel: EditTaxViewModel
    private lateinit var binding: EditTaxBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taxId = intent.getIntExtra("id", 0)
        viewModel = ViewModelProviders.of(this).get(EditTaxViewModel::class.java)
        viewModel.assignBtnEnable.value = taxId > 0

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_tax)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_dark)

        viewModel.apply {
            if (tax.value != null) {
                return
            }

            taxInput.value = taxId
        }


        edTaxName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.assignBtnEnable.value = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        btnAssignTax.setOnClickListener {
            val intent = Intent(this, AssignItemActivity::class.java)
            intent.putExtra("id", taxId)
            intent.putExtra("type", Item.AssignType.TAX)
            viewModel.checkedItemIds?.also {
                intent.putExtra("checked", it.toLongArray())
            }
            startActivityForResult(intent, ASSIGN_REQ)
        }

        btnDeleteTax.setOnClickListener {
            AlertUtil.showConfirmDelete(this, {
                viewModel.delete()
            }, {})
        }

        viewModel.saveSuccess.observe(this, Observer {
            if (it == true) {
                onBackPressed()
            }
        })

        viewModel.nameNotEmpty.observe(this, Observer {
            if (it == false) {
                binding.edTaxName.error = resources.getString(R.string.error_empty_input_format, "Category name")
            }
        })

        viewModel.nameUnique.observe(this, Observer {
            if (it == false) {
                binding.edTaxName.error = resources.getString(R.string.error_name_conflict_format, "Category name")
            }
        })

        viewModel.valueValid.observe(this, Observer {
            if (it == false) {
                binding.edTaxValue.error = "Invalid percentage"
            }
        })

        viewModel.deleteSuccess.observe(this, Observer {
            when (it) {
                false -> AlertUtil.showToast(this, resources.getString(R.string.fail_to_delete, "tax"))
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