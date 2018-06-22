package com.jsoft.pos.ui.views.category

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.CategoryVO
import com.jsoft.pos.data.model.CategorySearch
import com.jsoft.pos.ui.utils.LockHandler
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListFragment
import com.jsoft.pos.ui.views.SimpleListViewModel

class CategoriesFragment : SimpleListFragment<CategoryVO>() {

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

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = CategorySearch()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.setTitle(R.string.categories)
    }

    override val _adapter: SimpleListAdapter<CategoryVO>
        get() = adapter

    override val _viewModel: SimpleListViewModel<CategoryVO>
        get() = viewModel

    override fun onItemTouch(position: Int) {
        showEdit(adapter.getItemAt(position).id)
    }

    override fun showEdit(id: Any) {
        LockHandler.navigated(activity, true)

        val i = Intent(context, EditCategoryActivity::class.java)
        i.putExtra("id", id as Int)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }

    }

}
