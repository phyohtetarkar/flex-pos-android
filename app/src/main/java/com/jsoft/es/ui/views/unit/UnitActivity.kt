package com.jsoft.es.ui.views.unit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jsoft.es.R

class UnitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mode = intent.getSerializableExtra("mode") as? UnitsFragment.Mode

        supportFragmentManager.beginTransaction()
                .replace(R.id.contentUnit, UnitsFragment.getInstance(mode ?: UnitsFragment.Mode.SHOW))
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

}
