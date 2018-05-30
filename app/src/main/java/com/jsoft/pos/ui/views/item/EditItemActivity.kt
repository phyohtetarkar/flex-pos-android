package com.jsoft.pos.ui.views.item

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import com.jsoft.pos.R
import com.jsoft.pos.databinding.EditItemBinding
import com.jsoft.pos.ui.views.SimpleListDialogFragment
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
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_dark)

        edChooseCategory.onTouchDelegate = { showSelectDialog(DialogCategories())}
        edChooseUnit.onTouchDelegate = { showSelectDialog(DialogUnits()) }

        viewModel.apply {

            if (item.value != null) {
                return
            }

            itemInput.value = itemId

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (itemId > 0) {
            menuInflater.inflate(R.menu.menu_save_delete, menu)
        } else {
            menuInflater.inflate(R.menu.menu_save, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_save -> {
                viewModel.save()
                onBackPressed()
            }
            R.id.action_delete -> {
                viewModel.delete()
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun <T> showSelectDialog(fragment: SimpleListDialogFragment<T>) {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
        ft?.addToBackStack(null)

        fragment.show(ft, "dialog")
    }

}