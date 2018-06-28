package com.flex.pos.ui.views.category

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.flex.pos.FlexPosApplication
import com.flex.pos.MainActivity
import com.flex.pos.R
import com.flex.pos.data.entity.CategoryVO
import com.flex.pos.data.model.CategorySearch
import com.flex.pos.ui.utils.LockHandler
import com.flex.pos.ui.views.SimpleListAdapter
import com.flex.pos.ui.views.SimpleListFragment
import com.flex.pos.ui.views.SimpleListViewModel
import com.flex.pos.ui.views.lock.AutoLockActivity

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
        var id = 0

        if (position > -1) {
            id = adapter.getItemAt(position).id
        }

        (activity as? AutoLockActivity)?.navigated = true

        val i = Intent(context, EditCategoryActivity::class.java)
        i.putExtra("id", id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }
    }

}
