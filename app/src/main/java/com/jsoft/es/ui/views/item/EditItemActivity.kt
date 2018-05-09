package com.jsoft.es.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import com.jsoft.es.R
import com.jsoft.es.data.entity.Item
import com.jsoft.es.databinding.EditItemBinding
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItemActivity : AppCompatActivity() {

    private var itemId: Long = 0

    private lateinit var viewModel: EditItemViewModel
    private lateinit var binding: EditItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemId = intent.getLongExtra("id", 0)
        viewModel = ViewModelProviders.of(this).get(EditItemViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_item)
        binding.item = viewModel.item

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edChooseCategory.setOnKeyListener { _, _, _ -> false }
        edChooseCategory.setOnTouchListener { _, me ->

            if (me.action == MotionEvent.ACTION_DOWN) {
                showSelectDialog(SelectCategoryFragment())
            }

            true
        }

        viewModel.categoryLiveData.observe(this, Observer {
            viewModel.item.get()?.category = it
        })

        if (itemId > 0) {
            viewModel.itemLiveData.observe(this, Observer {
                viewModel.item.set(it)
                viewModel.categoryInput.value = it?.categoryId
            })
            viewModel.itemInput.value = itemId
        } else {
            viewModel.item.set(Item())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_save_2 -> {
                viewModel.save()
                onBackPressed()
            }
            R.id.action_delete -> {
                viewModel.delete()
                onBackPressed()
            }
        }

        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun showSelectDialog(fragment: DialogFragment) {
        val ft = supportFragmentManager?.beginTransaction()
        val prev = supportFragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
        ft?.addToBackStack(null)

        fragment.show(ft, "dialog")
    }

}