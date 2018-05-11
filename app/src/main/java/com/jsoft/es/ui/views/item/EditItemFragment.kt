package com.jsoft.es.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.jsoft.es.R
import com.jsoft.es.data.entity.Item
import kotlinx.android.synthetic.main.fragment_edit_item.*

class EditItemFragment : Fragment() {

    private var itemId: Long = 0

    private var viewModel: EditItemViewModel? = null
    private lateinit var binding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply { itemId = getLong("id") }
        viewModel = activity?.let { ViewModelProviders.of(it).get(EditItemViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_item, container, false)

        viewModel?.apply {
            categoryLiveData.observe(this@EditItemFragment, Observer {
                item.get()?.category = it
            })

            if (itemId > 0) {
                itemLiveData.observe(this@EditItemFragment, Observer {
                    item.set(it)
                    categoryInput.value = it?.categoryId
                })
                itemInput.value = itemId
            } else {
                item.set(Item())
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edChooseCategory.setOnKeyListener { _, _, _ -> false }
        edChooseCategory.setOnTouchListener { _, me ->

            if (me.action == MotionEvent.ACTION_DOWN) {
            }

            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_save_delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.action_save_2 -> {
                viewModel?.save()
                activity?.onBackPressed()
            }
            R.id.action_delete -> {
                viewModel?.delete()
                activity?.onBackPressed()
            }
        }

        return false
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