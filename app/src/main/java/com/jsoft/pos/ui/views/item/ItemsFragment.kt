package com.jsoft.pos.ui.views.item

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.data.model.ItemVOSearch
import com.jsoft.pos.ui.utils.LockHandler
import com.jsoft.pos.ui.views.AbstractListFragment
import com.jsoft.pos.ui.views.PagedListViewModel
import com.jsoft.pos.ui.views.SimplePagedListAdapter
import kotlinx.android.synthetic.main.fragment_items.*

class ItemsFragment : AbstractListFragment<ItemVO>() {

    private lateinit var adapter: SimplePagedListAdapter<ItemVO>
    private lateinit var viewModel: ItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)

        adapter = ItemVOAdapter(R.layout.layout_item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (fabItems.isShown) {
                        fabItems.hide()
                    }
                } else {
                    if (!fabItems.isShown) {
                        fabItems.show()
                    }
                }

            }
        })

        fabItems.setOnClickListener { onItemTouch(-1) }

    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = ItemVOSearch()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.setTitle(R.string.items)
    }

    override val recyclerView: RecyclerView
        get() = recyclerViewItems

    override val viewStub: View by lazy { viewStubItems.inflate() }

    override val _adapter: SimplePagedListAdapter<ItemVO>
        get() = adapter

    override val _viewModel: PagedListViewModel<ItemVO>
        get() = viewModel

    override fun onItemTouch(position: Int) {
        var id = 0L
        if (position > -1) {
            id = adapter.getItemAt(position).id
        }

        LockHandler.navigated(activity, true)

        val i = Intent(context, EditItemActivity::class.java)
        i.putExtra("id", id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }
    }

}