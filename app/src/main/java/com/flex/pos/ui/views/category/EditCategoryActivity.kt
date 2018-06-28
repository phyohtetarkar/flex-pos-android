package com.flex.pos.ui.views.category

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import com.flex.pos.R
import com.flex.pos.databinding.EditCategoryBinding
import com.flex.pos.ui.utils.AlertUtil
import com.flex.pos.ui.utils.ContextWrapperUtil
import com.flex.pos.ui.views.lock.AutoLockActivity
import kotlinx.android.synthetic.main.activity_edit_category.*

class EditCategoryActivity : AutoLockActivity() {

    private lateinit var viewModel: EditCategoryViewModel
    private lateinit var binding: EditCategoryBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(EditCategoryViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_category)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_dark)

        intent.getIntExtra("id", 0).also {
            if (it > 0) {
                supportActionBar?.setTitle(R.string.edit_category)
            } else {
                supportActionBar?.setTitle(R.string.create_category)
            }
        }

        btnDeleteCategory.setOnClickListener {
            AlertUtil.showConfirmDelete(this, {
                viewModel.delete()
            }, {})
        }

        viewModel.apply {

            if (category.value != null) {
                return
            }

            categoryInput.value = intent.getIntExtra("id", 0)
        }

        viewModel.colorChange.observe(this, Observer {
            toggleCheck(it)
        })

        viewModel.saveSuccess.observe(this, Observer {
            if (it == true) {
                onBackPressed()
            } else {
                AlertUtil.showToast(this, resources.getString(R.string.fail_to_save, "category"))
            }
        })

        viewModel.nameNotEmpty.observe(this, Observer {
            if (it == false) {
                binding.edCategoryName.error = resources.getString(R.string.error_empty_input_format, "Category name")
            }

        })

        viewModel.nameUnique.observe(this, Observer {
            if (it == false) {
                binding.edCategoryName.error = resources.getString(R.string.error_name_conflict_format, "Category name")
            }
        })

        viewModel.deleteSuccess.observe(this, Observer {
            when (it) {
                false -> AlertUtil.showToast(this, resources.getString(R.string.fail_to_delete, "category"))
                true -> onBackPressed()
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_save -> viewModel.save()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun toggleCheck(color: String?) {
        for (i in 0..constraintLayoutColorGroup.childCount) {
            val btn = constraintLayoutColorGroup.getChildAt(i) as? RadioButton
            btn?.isChecked = btn?.contentDescription?.toString().equals(color)
        }
    }

}
