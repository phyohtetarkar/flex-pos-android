package com.jsoft.pos.ui.views.receipt

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Sale
import com.jsoft.pos.data.model.SaleSearch
import com.jsoft.pos.ui.views.AbstractListFragment
import com.jsoft.pos.ui.views.BindingViewHolder
import com.jsoft.pos.ui.views.ListViewModel
import com.jsoft.pos.ui.views.SimplePagedListAdapter
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
        super.onViewCreated(view, savedInstanceState)

        recyclerViewSimpleList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple, resources.newTheme()))
            } else {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple))
            }
        })

        fabSimpleList.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.saleInput.value = SaleSearch()
    }

    override val recyclerView: RecyclerView
        get() = recyclerViewSimpleList

    override val viewStub: View by lazy { viewStubSimpleList.inflate() }

    override val _viewModel: ListViewModel<Sale>
    get() = viewModel

    override val _adapter: RecyclerView.Adapter<BindingViewHolder>
    get() = adapter

    override fun onItemTouch(position: Int) {
        val intent = Intent(context, ReceiptDetailActivity::class.java)
        intent.putExtra("id", adapter.getItemAt(position).id)
        intent.putExtra("showHomeUp", true)
        startActivity(intent)
    }

    companion object {
        val INSTANCE: ReceiptsFragment
            get() = ReceiptsFragment()
    }

}