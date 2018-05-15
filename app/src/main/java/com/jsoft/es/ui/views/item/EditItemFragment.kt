package com.jsoft.es.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.jsoft.es.BR
import com.jsoft.es.R
import com.jsoft.es.data.entity.Item
import kotlinx.android.synthetic.main.fragment_edit_item.*

class EditItemFragment : Fragment() {

    private var itemId: Long = 0

    private lateinit var viewModel: EditItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply { itemId = getLong("id") }
        viewModel = activity!!.let { ViewModelProviders.of(it).get(EditItemViewModel::class.java) }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_edit_item, container, false)
        binding.setVariable(BR.item, viewModel.item)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edChooseCategory.setOnKeyListener { _, _, _ -> false }
        edChooseCategory.setOnTouchListener { _, me ->

            if (me.action == MotionEvent.ACTION_DOWN) {
                showSelectDialog()
            }

            true
        }

        btnAddPrice.setOnClickListener {
            fragmentManager?.beginTransaction()
                    ?.addToBackStack(null)
                    ?.replace(R.id.contentEditItem, EditPriceFragment.getInstance(0), EditPriceFragment.TAG)
                    ?.commit()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.apply {

            if (item.get() != null) {
                return
            }

            categoryLiveData.observe(this@EditItemFragment, Observer {
                item.get()?.category = it
                item.notifyChange()
                categoryLiveData.removeObservers(this@EditItemFragment)
            })

            if (itemId > 0) {
                itemLiveData.observe(this@EditItemFragment,  Observer {
                    item.set(it)
                    categoryInput.value = it?.categoryId
                    itemLiveData.removeObservers(this@EditItemFragment)
                })
                itemInput.value = itemId
            } else {
                item.set(Item())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (itemId > 0) {
            inflater?.inflate(R.menu.menu_save_delete, menu)
        } else {
            inflater?.inflate(R.menu.menu_save, menu)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.action_save -> {
                viewModel.save()
                activity?.onBackPressed()
            }
            R.id.action_delete -> {
                viewModel.delete()
                activity?.onBackPressed()
            }
        }

        return false
    }

    private fun showSelectDialog() {
        val ft = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialogC")
        if (prev != null) {
            ft?.remove(prev)
        }
        ft?.addToBackStack(null)

        val frag = DialogCategories()
        frag.show(ft, "dialogC")
    }

    companion object {

        fun getInstance(id: Long): EditItemFragment {
            val frag = EditItemFragment()

            val bundle = Bundle()
            bundle.putLong("id", id)

            frag.arguments =bundle

            return frag
        }

    }

}