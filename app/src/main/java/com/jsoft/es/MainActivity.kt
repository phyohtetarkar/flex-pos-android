package com.jsoft.es

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.jsoft.es.ui.views.nav.ResourcesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mPendingRunnable: (() -> Unit)? = null

    private val drawerDelegate = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

        }

        override fun onDrawerOpened(drawerView: View) {

        }

        override fun onDrawerClosed(drawerView: View) {
            mPendingRunnable?.invoke()
            mPendingRunnable = null
        }

        override fun onDrawerStateChanged(newState: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarMain)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayoutMain, toolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayoutMain.addDrawerListener(toggle)
        drawerLayoutMain.addDrawerListener(drawerDelegate)
        toggle.syncState()

        val nav = findViewById<NavigationView>(R.id.navigationViewMain)
        nav.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        mPendingRunnable = {

            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("content")

            if (prev != null) {
                ft.remove(prev)
            }

            var fragment: Fragment? = null

            when (id) {
                R.id.action_pos -> {}
                R.id.action_resources -> fragment = ResourcesFragment()
                R.id.action_receipts -> {
                }
                R.id.action_statistics -> {
                }
            }

            if (fragment != null) {
                ft.replace(R.id.contentMain, fragment, "content")
            }

            ft.commit()

        }

        drawerLayoutMain.closeDrawer(GravityCompat.START)

        return true
    }

}
