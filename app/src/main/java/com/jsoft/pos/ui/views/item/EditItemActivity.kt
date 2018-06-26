package com.jsoft.pos.ui.views.item

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.data.entity.Tax
import com.jsoft.pos.databinding.EditItemBinding
import com.jsoft.pos.ui.custom.CustomViewAdapter
import com.jsoft.pos.ui.utils.AlertUtil
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import com.jsoft.pos.ui.utils.FileUtil
import com.jsoft.pos.ui.views.SimpleListDialogFragment
import com.jsoft.pos.ui.views.category.EditCategoryActivity
import com.jsoft.pos.ui.views.lock.AutoLockActivity
import com.jsoft.pos.ui.views.unit.EditUnitFragment
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItemActivity : AutoLockActivity() {

    private val PICKIMAGE = 1

    private lateinit var viewModel: EditItemViewModel
    private lateinit var binding: EditItemBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(EditItemViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_item)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_dark)

        edChooseCategory.onTouchDelegate = { showSelectDialog(DialogCategories())}
        edChooseUnit.onTouchDelegate = { showSelectDialog(DialogUnits()) }

        val taxAdapter = object : CustomViewAdapter<Tax>(linearLayoutTaxes, R.layout.layout_switch_item) {
            override fun onBindView(holder: SimpleViewHolder, position: Int) {
                holder.bind(list[position])
            }
        }

        val discountAdapter = object : CustomViewAdapter<Discount>(linearLayoutDiscounts, R.layout.layout_switch_item) {
            override fun onBindView(holder: SimpleViewHolder, position: Int) {
                holder.bind(list[position])
            }
        }

        viewModel.taxes.observe(this, Observer {
            taxAdapter.submitList(it)
        })

        viewModel.discounts.observe(this, Observer {
            discountAdapter.submitList(it)
        })

        viewModel.apply {

            if (item.value != null) {
                return
            }

            itemInput.value = intent.getLongExtra("id", 0)

        }

        viewModel.deleteSuccess.observe(this, Observer {
            when (it) {
                false -> AlertUtil.showToast(this, resources.getString(R.string.fail_to_delete, "item"))
                true -> onBackPressed()
            }
        })

        tvAddCategory.setOnClickListener {
            startActivity(Intent(this, EditCategoryActivity::class.java))
        }

        tvAddUnit.setOnClickListener {
            val ft = supportFragmentManager?.beginTransaction()
            val prev = supportFragmentManager?.findFragmentByTag("addUnitDialog")
            if (prev != null) {
                ft?.remove(prev)
            }
            val frag = EditUnitFragment.getInstance(0)
            frag.show(ft, "addUnitDialog")
        }

        btnAddImage.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            val intent = Intent.createChooser(getIntent, "Select Image")
            intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(intent, PICKIMAGE)
        }

        btnRemoveImage.setOnClickListener {
            imageViewItemImage.visibility = View.INVISIBLE
            btnRemoveImage.visibility = View.GONE
            btnAddImage.visibility = View.VISIBLE

            viewModel.item.value?.also {
                FileUtil.deleteImage(this@EditItemActivity, it.image)
                it.image = null
            }
        }

        btnDeleteItem.setOnClickListener {
            AlertUtil.showConfirmDelete(this, {
                viewModel.delete()
            }, {})
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
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
            PICKIMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.apply {

                        val path = FileUtil.writeImage(this@EditItemActivity, this, viewModel.item.value?.name)

                        viewModel.item.value?.image = path
                        imageViewItemImage.setImageBitmap(FileUtil.readImage(this@EditItemActivity, path))
                        imageViewItemImage.visibility = View.VISIBLE
                        btnRemoveImage.visibility = View.VISIBLE
                        btnAddImage.visibility = View.GONE
                    }

                }
            }
        }

    }

    private fun <T> showSelectDialog(fragment: SimpleListDialogFragment<T>) {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
        fragment.show(ft, "dialog")
    }

}