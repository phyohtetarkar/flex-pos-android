package com.flex.pos

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.flex.pos.ui.AboutActivity
import com.flex.pos.ui.utils.AlertUtil
import com.flex.pos.ui.utils.ContextWrapperUtil
import com.flex.pos.ui.utils.LockHandler
import com.flex.pos.ui.utils.ServiceLocator
import com.flex.pos.ui.views.discount.DiscountsFragment
import com.flex.pos.ui.views.lock.AutoLockActivity
import com.flex.pos.ui.views.nav.ResourcesFragment
import com.flex.pos.ui.views.receipt.ReceiptsFragment
import com.flex.pos.ui.views.sale.CheckOutItemsHolder
import com.flex.pos.ui.views.sale.SaleFragment
import com.flex.pos.ui.views.setting.SettingFragment
import com.flex.pos.ui.views.tax.TaxesFragment
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

        supportActionBar?.title = null

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

        navigationViewMain.menu.getItem(0).isChecked = true
        supportFragmentManager.beginTransaction()
                .replace(R.id.contentMain, ServiceLocator.locate(SaleFragment::class.java), "content")
                .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_about) {
            startActivity(Intent(this, AboutActivity::class.java))
            return true
        }

        if (id == R.id.action_rate) {
            val uri = Uri.parse("market://details?id=$packageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            } else {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            }

            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
            }

        }

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
                ft.commit()
            }

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
