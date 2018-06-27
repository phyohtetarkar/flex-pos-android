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
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.jsoft.pos.ui.utils.AlertUtil
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import com.jsoft.pos.ui.utils.LockHandler
import com.jsoft.pos.ui.utils.ServiceLocator
import com.jsoft.pos.ui.views.discount.DiscountsFragment
import com.jsoft.pos.ui.views.lock.AutoLockActivity
import com.jsoft.pos.ui.views.nav.ResourcesFragment
import com.jsoft.pos.ui.views.receipt.ReceiptsFragment
import com.jsoft.pos.ui.views.sale.CheckOutItemsHolder
import com.jsoft.pos.ui.views.sale.SaleFragment
import com.jsoft.pos.ui.views.setting.SettingFragment
import com.jsoft.pos.ui.views.tax.TaxesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_app_bar_main.*

class MainActivity : AutoLockActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        toolbarMain.setNavigationOnClickListener {
            when {
                CheckOutItemsHolder.onSale -> AlertUtil.showToast(this, R.string.active_sale)
                drawerLayoutMain.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_LOCKED_CLOSED -> {
                    animateToBurger()
                    super.onBackPressed()
                }
                else -> toggle()
            }
        }

        drawerLayoutMain.addDrawerListener(toggle)
        drawerLayoutMain.addDrawerListener(drawerDelegate)
        toggle.syncState()

        navigationViewMain.setNavigationItemSelectedListener(this)


        LockHandler.handle(this)

    }

    override fun onResume() {
        super.onResume()
        navigationViewMain.menu.getItem(0).isChecked = true
        onNavigationItemSelected(navigationViewMain.menu.findItem(R.id.action_pos))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        Log.v("TAG", item.toString())

        mPendingRunnable = {

            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("content")

            if (prev != null) {
                ft.remove(prev)
            }

            val fragment: Fragment? = when (id) {
                R.id.action_pos -> ServiceLocator.locate(SaleFragment::class.java)
                R.id.action_resources -> ServiceLocator.locate(ResourcesFragment::class.java)
                R.id.action_taxes -> ServiceLocator.locate(TaxesFragment::class.java)
                R.id.action_discounts -> ServiceLocator.locate(DiscountsFragment::class.java)
                R.id.action_receipts -> ServiceLocator.locate(ReceiptsFragment::class.java)
                R.id.action_setting -> ServiceLocator.locate(SettingFragment::class.java)
                else -> null
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

        if (LockHandler.backFreeze) {
            finish()
            return
        }

        if (drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutMain.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

        if (drawerLayoutMain.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            animateToBurger()
        }
    }

    fun animateToArrow() {
        val drawable = toggle.drawerArrowDrawable
        val animator = ObjectAnimator.ofFloat(drawable, "progress", 1f)
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}

            override fun onAnimationEnd(animator: Animator) {
                toggle.isDrawerIndicatorEnabled = false
                lockDrawer()
            }

            override fun onAnimationCancel(animator: Animator) {}

            override fun onAnimationRepeat(animator: Animator) {}
        })
        animator.start()

    }

    fun animateToBurger() {
        toggle.isDrawerIndicatorEnabled = true
        unlockDrawer()
        val drawable = toggle.drawerArrowDrawable
        val animator = ObjectAnimator.ofFloat(drawable, "progress", 0f)
        animator.start()
    }

    fun unlockDrawer() {
        drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun lockDrawer() {
        drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun toggle() {
        val drawerLockMode = drawerLayoutMain.getDrawerLockMode(GravityCompat.START)
        if (drawerLayoutMain.isDrawerVisible(GravityCompat.START) && drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN) {
            drawerLayoutMain.closeDrawer(GravityCompat.START)
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            drawerLayoutMain.openDrawer(GravityCompat.START)
        }
    }

}
