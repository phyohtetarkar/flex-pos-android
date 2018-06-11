package com.jsoft.pos.ui.views.charge

import android.app.Activity
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
import com.jsoft.pos.databinding.EditChargeBinding
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import kotlinx.android.synthetic.main.activity_edit_charge.*

class EditChargeActivity : AppCompatActivity() {

    private val ASSIGN_REQ = 1

    private var chargeId = 0

    private lateinit var viewModel: EditChargeViewModel
    private lateinit var binding: EditChargeBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chargeId = intent.getIntExtra("id", 0)
        viewModel = ViewModelProviders.of(this).get(EditChargeViewModel::class.java)
        viewModel.assignBtnEnable.value = chargeId > 0

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_charge)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_white)

        viewModel.apply {
            if (charge.value != null) {
                return
            }

            chargeInput.value = chargeId
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

        btnAssignCharge.setOnClickListener {
            val intent = Intent(this, AssignItemActivity::class.java)
            intent.putExtra("id", chargeId)
            intent.putExtra("type", Item.AssignType.CHARGE)
            viewModel.checkedItemIds?.also {
                intent.putExtra("checked", it.toLongArray())
            }
            startActivityForResult(intent, ASSIGN_REQ)
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