package com.jsoft.es.ui.views.item

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jsoft.es.R

class EditItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getLongExtra("id", 0)

        val frag = supportFragmentManager.findFragmentByTag(EditPriceFragment.TAG)

        supportFragmentManager.beginTransaction()
                .replace(R.id.contentEditItem, frag ?: EditItemFragment.getInstance(id))
                .commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return false
    }

}