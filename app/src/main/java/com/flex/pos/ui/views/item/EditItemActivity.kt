package com.flex.pos.ui.views.item

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.flex.pos.R
import com.flex.pos.data.entity.Discount
import com.flex.pos.data.entity.Tax
import com.flex.pos.databinding.EditItemBinding
import com.flex.pos.ui.custom.CustomViewAdapter
import com.flex.pos.ui.utils.AlertUtil
import com.flex.pos.ui.utils.ContextWrapperUtil
import com.flex.pos.ui.utils.FileUtil
import com.flex.pos.ui.views.SimpleListDialogFragment
import com.flex.pos.ui.views.category.EditCategoryActivity
import com.flex.pos.ui.views.lock.AutoLockActivity
import com.flex.pos.ui.views.unit.EditUnitFragment
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItemActivity : AutoLockActivity() {

    private val PICKIMAGE = 1
    private val WRITE_STORAGE_PERMISSION_FOR_PICKIMAGE_REQUEST = 2

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

        intent.getLongExtra("id", 0).also {
            if (it > 0) {
                supportActionBar?.setTitle(R.string.edit_item)
            } else {
                supportActionBar?.setTitle(R.string.create_item)
            }
        }

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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                invokePick()
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        WRITE_STORAGE_PERMISSION_FOR_PICKIMAGE_REQUEST)
            }


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

        initValidationObserve()

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
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_STORAGE_PERMISSION_FOR_PICKIMAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    invokePick()
                } else {
                    AlertUtil.showToast(this, "Permission denied, cannot load image")
                }
            }
        }
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

    private fun initValidationObserve() {

        viewModel.nameNotEmpty.observe(this, Observer {
            if (it == false) {
                binding.edItemName.error = resources.getString(R.string.error_empty_input_format, "Item name")
            }
        })

        viewModel.categoryNotEmpty.observe(this, Observer {
            if (it == false) {
                binding.edChooseCategory.error = resources.getString(R.string.error_empty_choice, "Category")
            }
        })

        viewModel.unitNotEmpty.observe(this, Observer {
            if (it == false) {
                binding.edChooseUnit.error = resources.getString(R.string.error_empty_choice, "Unit")
            }
        })

        viewModel.amountValid.observe(this, Observer {
            if (it == false) {
                binding.edItemAmount.error = resources.getString(R.string.error_not_valid, "Amount")
            }
        })

        viewModel.priceValid.observe(this, Observer {
            if (it == false) {
                binding.edItemPrice.error = resources.getString(R.string.error_not_valid, "Price")
            }
        })

        viewModel.barcodeValid.observe(this, Observer {
            if (it == false) {
                binding.edItemBarcode.error = resources.getString(R.string.error_name_conflict_format, "Barcode")
            }
        })

        viewModel.saveSuccess.observe(this, Observer {
            if (it == true) {
                onBackPressed()
            } else {
                AlertUtil.showToast(this, resources.getString(R.string.fail_to_save, "item"))
            }
        })

        viewModel.deleteSuccess.observe(this, Observer {
            when (it) {
                false -> AlertUtil.showToast(this, resources.getString(R.string.fail_to_delete, "item"))
                true -> onBackPressed()
            }
        })
    }

    private fun <T> showSelectDialog(fragment: SimpleListDialogFragment<T>) {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            ft?.remove(prev)
        }
        fragment.show(ft, "dialog")
    }

    private fun invokePick() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val intent = Intent.createChooser(getIntent, "Select Image")
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        navigated = true

        startActivityForResult(intent, PICKIMAGE)
    }

}