package com.jsoft.pos.ui.views.tax

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Item
import com.jsoft.pos.data.model.ItemSearch
import com.jsoft.pos.ui.custom.SimpleDividerItemDecoration
import com.jsoft.pos.ui.views.SimpleListAdapter
import kotlinx.android.synthetic.main.activity_checkable_list.*

class AssignItemTaxActivity : AppCompatActivity() {

    private var taxId: Int = 0

    private lateinit var viewModel: AssignItemTaxViewModel
    private lateinit var adapter: SimpleListAdapter<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkable_list)

        taxId = intent.getIntExtra("id", 0)

        viewModel = ViewModelProviders.of(this).get(AssignItemTaxViewModel::class.java)
        viewModel.taxId = taxId

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<Item>(){
            override fun areItemsTheSame(oldItem: Item?, newItem: Item?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Item?, newItem: Item?): Boolean {
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

        viewModel.items.observe(this, Observer {
            adapter.submitList(it)
            it?.apply {
                if (isEmpty()) {
                    stub.visibility = View.VISIBLE
                } else {
                    stub.visibility = View.INVISIBLE
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val result = Intent()
        result.putExtra("items", adapter.getCheckedItemIds().toLongArray())

        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.search.value = ItemSearch()
    }

}