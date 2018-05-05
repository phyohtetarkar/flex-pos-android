package com.jsoft.es.ui.views.item

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.jsoft.es.R

class EditItemPriceFragment : Fragment() {

    private var priceId: Long = 0

    private lateinit var viewModel: EditItemPriceViewModel
    private lateinit var binding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getLong("id")?.apply { priceId = this }

        viewModel = ViewModelProviders.of(this).get(EditItemPriceViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_price, container, false)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    companion object {

        const val UNIT_REQUEST_CODE = 1

        fun getInstance(id: Long): EditItemPriceFragment {
            val frag = EditItemPriceFragment()

            val args = Bundle()
            args.putLong("id", id)
            frag.arguments = args

            return frag
        }
    }

}