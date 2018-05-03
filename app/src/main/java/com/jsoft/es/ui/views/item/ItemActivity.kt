package com.jsoft.es.ui.views.item

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jsoft.es.R

class ItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val frag = supportFragmentManager.findFragmentByTag(ItemActivity.CONTENT)

        supportFragmentManager.beginTransaction()
                .replace(R.id.contentItem, frag ?: ItemsFragment.INSTANCE)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return false
    }

    companion object {
        internal const val CONTENT = "content"
    }

}