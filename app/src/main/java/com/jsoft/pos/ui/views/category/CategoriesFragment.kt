package com.jsoft.pos.ui.views.category

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.CategoryVO
import com.jsoft.pos.data.model.CategorySearch
import com.jsoft.pos.ui.custom.SimpleDividerItemDecoration
import com.jsoft.pos.ui.utils.RecyclerViewItemTouchListener
import com.jsoft.pos.ui.views.SimpleListAdapter
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {
    private lateinit var viewModel: CategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<CategoryVO>() {
            override fun areItemsTheSame(oldItem: CategoryVO, newItem: CategoryVO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CategoryVO, newItem: CategoryVO): Boolean {
                return oldItem == newItem
            }
        }, R.layout.layout_category)

        recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(SimpleDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 82))
            addOnItemTouchListener(RecyclerViewItemTouchListener(this, object : RecyclerViewItemTouchListener.OnTouchListener {
                override fun onTouch(view: View, position: Int) {
                    val (id) = adapter.getItemAt(position)
                    showEdit(id)
                }

                override fun onLongTouch(view: View, position: Int) {

                }
            }))

            this.adapter = adapter
        }

        fabCategories.setOnClickListener { showEdit(0) }

        val stub = viewStubCategories.inflate()

        viewModel.categories.observe(this, Observer {
            adapter.submitList(it)
            it?.apply {
                if (isEmpty()) {
                    stub.visibility = View.VISIBLE
                } else {
                    stub.visibility = View.GONE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = CategorySearch()
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
