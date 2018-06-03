package com.jsoft.pos.ui.views.tax

import android.app.Activity
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

class AssignItemActivity : AppCompatActivity() {

    enum class AssignType {
        TAX, DISCOUNT
    }

    private lateinit var viewModel: AssignItemViewModel
    private lateinit var adapter: SimpleListAdapter<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkable_list)

        viewModel = ViewModelProviders.of(this).get(AssignItemViewModel::class.java)
        viewModel.id = intent.getIntExtra("id", 0)
        viewModel.type = intent.getSerializableExtra("type") as AssignType
        viewModel.checkedIds = intent.getLongArrayExtra("checked").asList()

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
            layoutManager = LinearLayoutManager(this@AssignItemActivity)
            setHasFixedSize(true)
            addItemDecoration(SimpleDividerItemDecoration(this@AssignItemActivity, DividerItemDecoration.VERTICAL))

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
                    stub.visibility = View.GONE
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_save -> {
                val data = Intent()
                data.putExtra("items", adapter.getCheckedItemIds().toLongArray())
                setResult(Activity.RESULT_OK, data)
                onBackPressed()
            }
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.search.value = ItemSearch()
    }

}