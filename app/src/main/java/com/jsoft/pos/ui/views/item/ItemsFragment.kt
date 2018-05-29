package com.jsoft.pos.ui.views.item

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.data.model.ItemSearch
import com.jsoft.pos.ui.views.PagedListViewModel
import com.jsoft.pos.ui.views.SimplePagedListAdapter
import com.jsoft.pos.ui.views.SimplePagedListFragment
import kotlinx.android.synthetic.main.fragment_items.*

class ItemsFragment : SimplePagedListFragment<ItemVO>() {

    private lateinit var adapter: SimplePagedListAdapter<ItemVO>
    private lateinit var viewModel: ItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)

        adapter = SimplePagedListAdapter(object : DiffUtil.ItemCallback<ItemVO>() {
            override fun areItemsTheSame(oldItem: ItemVO, newItem: ItemVO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemVO, newItem: ItemVO): Boolean {
                return oldItem == newItem
            }
        }, R.layout.layout_item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabItems.setOnClickListener { showEdit(0) }

    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = ItemSearch()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as MainActivity
        activity.setTitle(R.string.title_items)
    }

    override val recyclerView: RecyclerView
        get() = recyclerViewItems

    override val viewStub: View by lazy { viewStubItems.inflate() }

    override val _adapter: SimplePagedListAdapter<ItemVO>
        get() = adapter

    override val _viewModel: PagedListViewModel<ItemVO>
        get() = viewModel

    override fun onItemTouch(position: Int) {
        showEdit(adapter.getItemAt(position).id)
    }

    private fun showEdit(id: Long) {
        val i = Intent(context, EditItemActivity::class.java)
        i.putExtra("id", id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }

    }

    companion object {
        val INSTANCE: ItemsFragment
            get() = ItemsFragment()
    }

}