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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_save_delete, menu)
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    companion object {
        fun getInstance(id: Long): EditItemFragment {
            val frag = EditItemFragment()

            val args = Bundle()
            args.putLong("id", id)
            frag.arguments = args

            return frag
        }
    }

}