package com.flex.pos.ui.views.setting

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.flex.pos.FlexPosApplication
import com.flex.pos.R
import com.flex.pos.ui.utils.AlertUtil
import com.flex.pos.ui.views.lock.AutoLockActivity
import kotlinx.android.synthetic.main.activity_backup.*

class BackupActivity : AutoLockActivity() {

    private var adapter: BackupAdapter? = null
    private var viewModel: BackupViewModel? = null

    private val viewStub: View by lazy { viewStubBackup.inflate() }

    private val READ_STORAGE_PERMISSION_FOR_LOAD_BACKUP_REQUEST = 1
    private val WRITE_STORAGE_PERMISSION_FOR_BACKUP_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_dark)
        supportActionBar?.setTitle(R.string.backups)

        adapter = BackupAdapter()

        recyclerViewBackup.apply {
            layoutManager = LinearLayoutManager(this@BackupActivity)
            setHasFixedSize(true)
        }

        recyclerViewBackup.adapter = adapter

        adapter?.onRestore = {
            viewModel?.restoreBackup(adapter?.getItemAt(it))
        }

        adapter?.onDelete = {
            AlertUtil.showConfirmDelete(this@BackupActivity, {
                viewModel?.deleteBackup(adapter?.getItemAt(it))
                viewModel?.loadBackups()
            }, {

            })
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
                val app = application as FlexPosApplication
                app.loadDatabase()
                AlertUtil.showToast(this, "Restore success")
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            viewModel?.loadBackups()
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_FOR_LOAD_BACKUP_REQUEST)
        }
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
                            WRITE_STORAGE_PERMISSION_FOR_BACKUP_REQUEST)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_STORAGE_PERMISSION_FOR_BACKUP_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel?.saveBackup()
                } else {
                    AlertUtil.showToast(this, "Permission denied, cannot backup data")
                }
            }

            READ_STORAGE_PERMISSION_FOR_LOAD_BACKUP_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel?.loadBackups()
                } else {
                    AlertUtil.showToast(this, "Permission denied, cannot load backups")
                }
            }
        }
    }

}