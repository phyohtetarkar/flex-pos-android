package com.jsoft.pos

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import com.jsoft.pos.ui.views.nav.ResourcesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mPendingRunnable: (() -> Unit)? = null
    private lateinit var toggle: ActionBarDrawerToggle

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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarMain)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle = ActionBarDrawerToggle(
                this, drawerLayoutMain, toolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        toggle.setToolbarNavigationClickListener {
            animateToBurger()
            super.onBackPressed()
        }

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
                R.id.action_pos -> {
                }
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

    override fun setTitle(titleId: Int) {
        toolbarMain.setTitle(titleId)
    }

    override fun onBackPressed() {
        if (drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutMain.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed()
        }

        if (drawerLayoutMain.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            animateToBurger();
        }
    }

    fun animateToArrow() {
        val drawable = toggle.drawerArrowDrawable
        val animator = ObjectAnimator.ofFloat(drawable, "progress", 1f)
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}

            override fun onAnimationEnd(animator: Animator) {
                toggle.isDrawerIndicatorEnabled = false
                drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onAnimationCancel(animator: Animator) {}

            override fun onAnimationRepeat(animator: Animator) {}
        })
        animator.start()

    }

    fun animateToBurger() {
        toggle.setDrawerIndicatorEnabled(true)
        drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        val drawable = toggle.drawerArrowDrawable
        val animator = ObjectAnimator.ofFloat(drawable, "progress", 0f)
        animator.start()
    }

}
