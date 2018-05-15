package com.jsoft.es.ui.views.item

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.jsoft.es.R

class EditPriceFragment : Fragment() {

    private var priceId: Long = 0

    private var viewModel: EditItemViewModel? = null
    private lateinit var binding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply { priceId = getLong("id") }
        viewModel = activity?.let { ViewModelProviders.of(it).get(EditItemViewModel::class.java) }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_price, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_save_delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.action_save -> {
                activity?.onBackPressed()
            }
        }

        return false
    }

    companion object {

        const val TAG = "EditPriceFragment"

        fun getInstance(id: Long): EditPriceFragment {
            val frag = EditPriceFragment()

            val bundle = Bundle()
            bundle.putLong("id", id)

            frag.arguments =bundle

            return frag
        }

    }

}