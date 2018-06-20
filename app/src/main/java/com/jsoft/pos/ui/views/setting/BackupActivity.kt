package com.jsoft.pos.ui.views.setting

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jsoft.pos.R
import com.jsoft.pos.ui.utils.AlertUtil
import kotlinx.android.synthetic.main.activity_backup.*

class BackupActivity : AppCompatActivity() {

    private var adapter: BackupAdapter? = null
    private var viewModel: BackupViewModel? = null

    private val viewStub: View by lazy { viewStubBackup.inflate() }

    private val WRITE_STORAGE_PERMISSION_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_dark)

        adapter = BackupAdapter()

        recyclerViewBackup.apply {
            layoutManager = LinearLayoutManager(this@BackupActivity)
            setHasFixedSize(true)
        }

        recyclerViewBackup.adapter = adapter

        adapter?.backupActionListener = object : BackupAdapter.BackupActionListener {
            override fun restore(position: Int) {
                viewModel?.restoreBackup(adapter?.getItemAt(position))
            }

            override fun delete(position: Int) {
                AlertUtil.showConfirmDelete(this@BackupActivity, {
                    viewModel?.deleteBackup(adapter?.getItemAt(position))
                    viewModel?.loadBackups()
                }, {

                })

            }

        }

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
                AlertUtil.showToast(this, "Please restart application to take effect")
            } else {
                AlertUtil.showToast(this, "Restore failed")
            }
        })

        viewModel?.deleteSuccess?.observe(this, Observer {
            if (it == true) {
                AlertUtil.showToast(this, "Delete success")
                viewModel?.loadBackups()
            } else {
                AlertUtil.showToast(this, "Delete failed")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel?.loadBackups()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_backup, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_backup -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    viewModel?.saveBackup()
                } else {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            WRITE_STORAGE_PERMISSION_REQUEST)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_STORAGE_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel?.saveBackup()
                } else {
                    AlertUtil.showToast(this, "Permission denied, cannot backup data")
                }
            }
        }
    }

}