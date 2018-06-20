package com.jsoft.pos.ui.views.setting

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jsoft.pos.R
import com.jsoft.pos.ui.utils.AlertUtil
import com.jsoft.pos.ui.utils.RecyclerViewItemTouchListener
import com.jsoft.pos.ui.views.SimpleListAdapter
import kotlinx.android.synthetic.main.fragment_simple_list.*

class BackupActivity : AppCompatActivity() {

    private var adapter: SimpleListAdapter<String>? = null
    private var viewModel: BackupViewModel? = null

    private val viewStub: View by lazy { viewStubSimpleList.inflate() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_simple_list)

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String?, newItem: String?): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String?, newItem: String?): Boolean {
                return oldItem == newItem
            }
        })

        fabSimpleList.visibility = View.GONE

        recyclerViewSimpleList.apply {
            layoutManager = LinearLayoutManager(this@BackupActivity)
            setHasFixedSize(true)
            addOnItemTouchListener(RecyclerViewItemTouchListener(this@BackupActivity, this, object : RecyclerViewItemTouchListener.OnTouchListener {
                override fun onTouch(view: View, position: Int) {

                }

                override fun onLongTouch(view: View, position: Int) {

                }

            }))
        }

        recyclerViewSimpleList.adapter = adapter

        recyclerViewSimpleList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple, resources.newTheme()))
            } else {
                it.setDrawable(resources.getDrawable(R.drawable.divider_simple))
            }
        })

        viewModel = ViewModelProviders.of(this).get(BackupViewModel::class.java)

        viewModel?.backups?.observe(this, Observer {
            adapter?.submitList(it)
            it?.apply {
                if (isEmpty()) {
                    viewStub.visibility = View.VISIBLE
                } else {
                    viewStub.visibility = View.GONE
                }
            }
        })

        viewModel?.backupSuccess?.observe(this, Observer {
            if (it == true) {
                AlertUtil.showToast(this, "Backup success")
                viewModel?.loadBackups()
            } else {
                AlertUtil.showToast(this, "Backup failed")
            }
        })

        viewModel?.restoreSuccess?.observe(this, Observer {
            if (it == true) {
                AlertUtil.showToast(this, "Restore success")
            } else {
                AlertUtil.showToast(this, "Backup failed")
            }
        })
    }

}