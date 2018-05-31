package com.jsoft.pos.ui.views.tax

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.ItemJoinVO
import com.jsoft.pos.ui.custom.SimpleDividerItemDecoration
import com.jsoft.pos.ui.views.SimpleListAdapter
import kotlinx.android.synthetic.main.activity_checkable_list.*

class AssignItemTaxActivity : AppCompatActivity() {

    private lateinit var viewModel: AssignItemTaxViewModel
    private lateinit var adapter: SimpleListAdapter<ItemJoinVO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkable_list)

        viewModel = ViewModelProviders.of(this).get(AssignItemTaxViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<ItemJoinVO>(){
            override fun areItemsTheSame(oldItem: ItemJoinVO?, newItem: ItemJoinVO?): Boolean {
                return oldItem?.itemId == newItem?.itemId
            }

            override fun areContentsTheSame(oldItem: ItemJoinVO?, newItem: ItemJoinVO?): Boolean {
                return oldItem == newItem
            }

        }, R.layout.layout_checkable_item)

        recyclerViewCheckableList.apply {
            layoutManager = LinearLayoutManager(this@AssignItemTaxActivity)
            setHasFixedSize(true)
            addItemDecoration(SimpleDividerItemDecoration(this@AssignItemTaxActivity, DividerItemDecoration.VERTICAL))

        }

        recyclerViewCheckableList.adapter = adapter
        checkboxAll.setOnCheckedChangeListener { _, isChecked ->
            adapter.toggleCheck(isChecked)
        }

        val stub = viewStubList.inflate()

    }

}