package com.jsoft.es.ui.views.category

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.RadioButton
import com.jsoft.es.BR
import com.jsoft.es.R
import com.jsoft.es.data.entity.Category
import com.jsoft.es.func.KConsumer2
import com.jsoft.es.ui.utils.ValidatorUtils
import kotlinx.android.synthetic.main.fragment_edit_category.*

class EditCategoryFragment : Fragment() {

    private var categoryId = 0

    private lateinit var viewModel: EditCategoryViewModel
    private lateinit var binding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getInt("id")?.apply { categoryId = this }

        viewModel = ViewModelProviders.of(this).get(EditCategoryViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_category, container, false)
        binding.setVariable(BR.isValidCategoryName, true)
        binding.setVariable(BR.category, viewModel.category)

        val handler: KConsumer2<View, Category> = object : KConsumer2<View, Category> {

            override fun accept(var1: View, var2: Category) {
                onColorSelect(var1, var2)
            }

        }

        binding.setVariable(BR.colorSelectHandler, handler)

        if (categoryId > 0) {
            viewModel.categoryLive.observe(this, Observer { viewModel.category.set(it) })
            viewModel.categoryInput.value = categoryId
        } else {
            viewModel.category.set(Category())
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_category, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_category_save -> {
                val category = viewModel.category.get()

                val valid = ValidatorUtils.isValid(category?.name, ValidatorUtils.NOT_EMPTY)

                if (!valid) {
                    binding.setVariable(BR.isValidCategoryName, false)
                } else {
                    viewModel.save()
                    activity?.onBackPressed()
                }
            }
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
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

    companion object {
        fun getInstance(id: Int): EditCategoryFragment {
            val frag = EditCategoryFragment()

            val args = Bundle()
            args.putInt("id", id)
            frag.arguments = args

            return frag
        }
    }
}
