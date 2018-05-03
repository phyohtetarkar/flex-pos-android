package com.jsoft.es.ui.views.category

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jsoft.es.R

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val frag = supportFragmentManager.findFragmentByTag(CONTENT)

        val mode = intent.getSerializableExtra("mode") as? CategoriesFragment.Mode

        supportFragmentManager.beginTransaction()
                .replace(R.id.contentCategory, frag ?: CategoriesFragment.getInstance( mode ?: CategoriesFragment.Mode.SHOW))
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
