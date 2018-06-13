package com.jsoft.pos.ui.views.category

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.databinding.EditCategoryBinding
import com.jsoft.pos.func.KConsumer2
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import kotlinx.android.synthetic.main.activity_edit_category.*

class EditCategoryActivity : AppCompatActivity() {

    private var categoryId = 0

    private lateinit var viewModel: EditCategoryViewModel
    private lateinit var binding: EditCategoryBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryId = intent.getIntExtra("id", 0)
        viewModel = ViewModelProviders.of(this).get(EditCategoryViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_category)
        binding.setLifecycleOwner(this)
        binding.isValidCategoryName = true
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_white)

        val handler: KConsumer2<View, Category> = object : KConsumer2<View, Category> {

            override fun accept(var1: View, var2: Category) {
                onColorSelect(var1, var2)
            }

        }

        binding.colorSelectHandler = handler

        viewModel.apply {

            if (category.value != null) {
                return
            }

            categoryInput.value = categoryId
        }

        viewModel.saveSuccess.observe(this, Observer {
            if (it == true) {
                onBackPressed()
            }
        })

        viewModel.nameValid.observe(this, Observer {
            binding.isValidCategoryName = it ?: true
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
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

    private fun onColorSelect(view: View, c: Category) {
        val btn = view as RadioButton
        val color = btn.contentDescription.toString()
        c.color = color
        toggleCheck(color)
    }

    private fun toggleCheck(color: String) {
        for (i in 0..constraintLayoutColorGroup.childCount) {
            val btn = constraintLayoutColorGroup.getChildAt(i) as? RadioButton
            btn?.isChecked = btn?.contentDescription?.toString().equals(color)
        }
    }

}
