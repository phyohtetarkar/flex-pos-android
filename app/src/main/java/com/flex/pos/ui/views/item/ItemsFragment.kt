package com.flex.pos.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.flex.pos.MainActivity
import com.flex.pos.R
import com.flex.pos.data.entity.Category
import com.flex.pos.data.entity.ItemVO
import com.flex.pos.data.model.ItemVOSearch
import com.flex.pos.ui.utils.Utils
import com.flex.pos.ui.views.AbstractListFragment
import com.flex.pos.ui.views.PagedListViewModel
import com.flex.pos.ui.views.SimplePagedListAdapter
import com.flex.pos.ui.views.lock.AutoLockActivity
import kotlinx.android.synthetic.main.fragment_items.*
import kotlinx.android.synthetic.main.layout_app_bar_main.*

class ItemsFragment : AbstractListFragment<ItemVO>() {

    private lateinit var adapter: SimplePagedListAdapter<ItemVO>
    private lateinit var spinnerAdapter: ArrayAdapter<Category>
    private lateinit var viewModel: ItemsViewModel

    private var mSpinner: AppCompatSpinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)

        adapter = ItemVOAdapter(R.layout.layout_item)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater?.inflate(R.menu.menu_search, menu)

        val searchView = Utils.initSearchView(activity, menu)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchModel.value?.name = newText
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.categories.observe(this, Observer {
            spinnerAdapter.clear()
            spinnerAdapter.add(Category(name = "All Item"))
            spinnerAdapter.addAll(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = ItemVOSearch()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.supportActionBar?.title = null

        mSpinner = LayoutInflater.from(context).inflate(R.layout.layout_toolbar_spinner, app.toolbarMain, false) as AppCompatSpinner?
        spinnerAdapter = ArrayAdapter(app.appbar_main.context, R.layout.extended_simple_list_item_1)
        mSpinner?.adapter = spinnerAdapter
        mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.searchModel.value?.categoryId = spinnerAdapter.getItem(position).id
            }

        }

        app.toolbarMain.addView(mSpinner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.toolbarMain?.removeView(mSpinner)
        mSpinner = null
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

        (activity as? AutoLockActivity)?.navigated = true

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