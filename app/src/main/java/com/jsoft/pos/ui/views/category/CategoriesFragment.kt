package com.jsoft.pos.ui.views.category

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
import com.jsoft.pos.data.entity.CategoryVO
import com.jsoft.pos.data.model.CategorySearch
import com.jsoft.pos.ui.custom.SimpleDividerItemDecoration
import com.jsoft.pos.ui.views.AbstractListFragment
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListViewModel
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_units.*

class CategoriesFragment : AbstractListFragment<CategoryVO>() {

    private lateinit var adapter: SimpleListAdapter<CategoryVO>
    private lateinit var viewModel: CategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<CategoryVO>() {
            override fun areItemsTheSame(oldItem: CategoryVO, newItem: CategoryVO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CategoryVO, newItem: CategoryVO): Boolean {
                return oldItem == newItem
            }
        }, R.layout.layout_category)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewCategories.addItemDecoration(SimpleDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 82))

        fabCategories.setOnClickListener { showEdit(0) }

    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = CategorySearch()
    }

    override val recyclerView: RecyclerView
        get() = recyclerViewCategories

    override val viewStub: View by lazy { viewStubUnits.inflate() }

    override val _adapter: SimpleListAdapter<CategoryVO>
        get() = adapter

    override val _viewModel: SimpleListViewModel<CategoryVO>
        get() = viewModel

    override fun onItemTouch(position: Int) {
        showEdit(adapter.getItemAt(position).id)
    }

    private fun showEdit(id: Int) {
        val i = Intent(context, EditCategoryActivity::class.java)
        i.putExtra("id", id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }

    }

    companion object {
        val INSTANCE: CategoriesFragment
            get() {
                return CategoriesFragment()
            }
    }

}
