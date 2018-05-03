package com.jsoft.es.ui.views.item

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.jsoft.es.BR
import com.jsoft.es.R
import com.jsoft.es.data.entity.Item
import com.jsoft.es.ui.views.category.CategoriesFragment
import com.jsoft.es.ui.views.category.CategoryActivity
import com.jsoft.es.ui.views.category.EditCategoryFragment
import com.jsoft.es.ui.views.unit.UnitActivity
import com.jsoft.es.ui.views.unit.UnitsFragment
import kotlinx.android.synthetic.main.fragment_edit_item.*

class EditItemFragment : Fragment() {

    private var itemId: Long = 0

    private lateinit var viewModel: EditItemViewModel
    private lateinit var binding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getLong("id")?.apply { itemId = this }

        viewModel = ViewModelProviders.of(this).get(EditItemViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_item, container, false)
        binding.setVariable(BR.item, viewModel.item)

        viewModel.categoryLiveData.observe(this, Observer {
            viewModel.item.get()?.category = it
            viewModel.item.notifyChange()
        })

        viewModel.unitLiveData.observe(this, Observer {
            viewModel.item.get()?.unit = it
            viewModel.item.notifyChange()
        })

        if (itemId > 0) {
            viewModel.itemLiveData.observe(this, Observer {
                viewModel.item.set(it)
                viewModel.categoryInput.value = it?.categoryId
                viewModel.unitInput.value = it?.unitId
            })
            viewModel.itemInput.value = itemId
        } else {
            viewModel.item.set(Item())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edEditItemCategory.setOnKeyListener { p0, p1, p2 -> true}
        edEditItemCategory.setOnTouchListener { _, me ->
            edEditItemCategory.requestFocus()
            if (me.action == MotionEvent.ACTION_DOWN) {
                val i = Intent(activity, CategoryActivity::class.java)
                i.putExtra("mode", CategoriesFragment.Mode.SELECT)
                startActivityForResult(i, CATEGORY_REQUEST_CODE)
            }
            true
        }

        edEditItemUnit.setOnKeyListener { p0, p1, p2 -> true}
        edEditItemUnit.setOnTouchListener { _, me ->
            edEditItemUnit.requestFocus()
            if (me.action == MotionEvent.ACTION_DOWN) {
                val i = Intent(activity, UnitActivity::class.java)
                i.putExtra("mode", UnitsFragment.Mode.SELECT)
                startActivityForResult(i, UNIT_REQUEST_CODE)
            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.action_item_save -> {
                viewModel.save()

                activity?.onBackPressed()
            }

            R.id.action_item_delete -> {
                viewModel.delete()

                activity?.onBackPressed()
            }

        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            CATEGORY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val categoryId = data!!.getIntExtra("categoryId", 0)
                    viewModel.item.get()?.categoryId = categoryId
                    viewModel.categoryInput.value = categoryId
                }
            }

            UNIT_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val unitId = data!!.getIntExtra("unitId", 0)
                    viewModel.item.get()?.unitId = unitId
                    viewModel.unitInput.value = unitId
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    companion object {

        const val CATEGORY_REQUEST_CODE = 0
        const val UNIT_REQUEST_CODE = 1

        fun getInstance(id: Long): EditItemFragment {
            val frag = EditItemFragment()

            val args = Bundle()
            args.putLong("id", id)
            frag.arguments = args

            return frag
        }
    }

}