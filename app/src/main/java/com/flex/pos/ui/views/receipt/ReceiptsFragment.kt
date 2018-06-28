package com.flex.pos.ui.views.receipt

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flex.pos.MainActivity
import com.flex.pos.R
import com.flex.pos.data.entity.Sale
import com.flex.pos.data.model.SaleSearch
import com.flex.pos.ui.views.AbstractListFragment
import com.flex.pos.ui.views.BindingViewHolder
import com.flex.pos.ui.views.ListViewModel
import com.flex.pos.ui.views.SimplePagedListAdapter
import com.flex.pos.ui.views.lock.AutoLockActivity
import kotlinx.android.synthetic.main.fragment_simple_list.*

class ReceiptsFragment : AbstractListFragment<Sale>() {

    private lateinit var viewModel: ReceiptsViewModel
    private lateinit var adapter: SimplePagedListAdapter<Sale>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReceiptsViewModel::class.java)
        adapter = SimplePagedListAdapter(object : DiffUtil.ItemCallback<Sale>() {
            override fun areItemsTheSame(oldItem: Sale?, newItem: Sale?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Sale?, newItem: Sale?): Boolean {
                return oldItem == newItem
            }

        }, R.layout.layout_receipt)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewSimpleList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple, resources.newTheme()))
            } else {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple))
            }
        })

        fabSimpleList.visibility = View.GONE

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.saleInput.value = SaleSearch()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.setTitle(R.string.receipts)
    }

    override val recyclerView: RecyclerView
        get() = recyclerViewSimpleList

    override val viewStub: View by lazy { viewStubSimpleList.inflate() }

    override val _viewModel: ListViewModel<Sale>
    get() = viewModel

    override val _adapter: RecyclerView.Adapter<BindingViewHolder>
    get() = adapter

    override fun onItemTouch(position: Int) {

        if (position == -1) {
            return
        }

        (activity as? AutoLockActivity)?.navigated = true

        val intent = Intent(context, ReceiptDetailActivity::class.java)
        intent.putExtra("id", adapter.getItemAt(position).id)
        intent.putExtra("historyMode", true)
        startActivity(intent)
    }

}