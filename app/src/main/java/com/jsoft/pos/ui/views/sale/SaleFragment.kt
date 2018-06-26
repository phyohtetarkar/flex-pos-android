package com.jsoft.pos.ui.views.sale

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.ObservableList
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.DisplayMetrics
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.data.model.ItemVOSearch
import com.jsoft.pos.ui.custom.BadgeDrawable
import com.jsoft.pos.ui.custom.RoundedImageView
import com.jsoft.pos.ui.utils.LockHandler
import com.jsoft.pos.ui.utils.Utils
import com.jsoft.pos.ui.views.BindingViewHolder
import com.jsoft.pos.ui.views.ListViewModel
import com.jsoft.pos.ui.views.SimpleListFragment
import com.jsoft.pos.ui.views.SimplePagedListAdapter
import com.jsoft.pos.ui.views.item.ItemVOAdapter
import kotlinx.android.synthetic.main.fragment_simple_list.*
import kotlinx.android.synthetic.main.layout_app_bar_main.*
import kotlinx.android.synthetic.main.layout_item_compact.view.*
import kotlin.math.roundToInt

class SaleFragment : SimpleListFragment<ItemVO>() {

    private lateinit var adapter: SimplePagedListAdapter<ItemVO>
    private lateinit var spinnerAdapter: ArrayAdapter<Category>
    private lateinit var viewModel: SaleViewModel
    private var mSpinner: AppCompatSpinner? = null
    private var icon: LayerDrawable? = null

    private val receiptPosition = IntArray(2)

    private val listChangeListener = object : ObservableList.OnListChangedCallback<ObservableList<SaleItem>>() {
        override fun onItemRangeRemoved(sender: ObservableList<SaleItem>?, positionStart: Int, itemCount: Int) {
            updateBadgeCount()
        }

        override fun onItemRangeMoved(sender: ObservableList<SaleItem>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            updateBadgeCount()
        }

        override fun onItemRangeInserted(sender: ObservableList<SaleItem>?, positionStart: Int, itemCount: Int) {
            updateBadgeCount()
        }

        override fun onItemRangeChanged(sender: ObservableList<SaleItem>?, positionStart: Int, itemCount: Int) {
            updateBadgeCount()
        }

        override fun onChanged(sender: ObservableList<SaleItem>?) {
            updateBadgeCount()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SaleViewModel::class.java)
        adapter = ItemVOAdapter(R.layout.layout_item_compact)

        setHasOptionsMenu(true)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.toolbarMain.title = ""

        mSpinner = LayoutInflater.from(context).inflate(R.layout.layout_toolbar_spinner, app.toolbarMain, false) as AppCompatSpinner?
        spinnerAdapter = ArrayAdapter(app.appbar_main.context, R.layout.extended_simple_list_item_1)
        mSpinner?.adapter = spinnerAdapter
        mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.itemSearch.value?.categoryId = spinnerAdapter.getItem(position).id
            }

        }

        app.toolbarMain.addView(mSpinner)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fabSimpleList.visibility = View.GONE

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.categories.observe(this, Observer {
            spinnerAdapter.clear()
            spinnerAdapter.add(Category(name = "All Item"))
            spinnerAdapter.addAll(it)
        })

        CheckOutItemsHolder.addOnListChangeListener(listChangeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_sale, menu)

        icon = menu?.findItem(R.id.action_receipt)?.icon as? LayerDrawable

        Handler().post {
            val v = activity?.findViewById<View>(R.id.action_receipt)
            v?.getLocationOnScreen(receiptPosition)
        }

        val searchView = Utils.initSearchView(activity, menu)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.itemSearch.value?.name = newText
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.action_receipt -> {
                if (CheckOutItemsHolder.itemCount > 0) {
                    LockHandler.navigated(activity, true)
                    val intent = Intent(context, CheckoutActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.itemSearch.value = ItemVOSearch().also { it.isAvailable = true }

        val app = activity as MainActivity
        if (!CheckOutItemsHolder.onSale) {
            app.unlockDrawer()
        } else {
            updateBadgeCount()
        }
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.toolbarMain?.removeView(mSpinner)
        CheckOutItemsHolder.removeOnListChangeListener(listChangeListener)
        mSpinner = null
    }

    override fun onDestroy() {
        super.onDestroy()

        CheckOutItemsHolder.clear()
    }

    override val _viewModel: ListViewModel<ItemVO>
        get() = viewModel

    override val _adapter: RecyclerView.Adapter<BindingViewHolder>
        get() = adapter

    override fun onItemTouch(position: Int) {

    }

    override fun onItemTouch(view: View, position: Int) {

        if (position == -1) {
            return
        }

        val itemVO = adapter.getItemAt(position)

        val origin = view.roundedImageView

        val copy = RoundedImageView(activity!!)
        copy.setImageBitmap((origin.drawable as BitmapDrawable).bitmap)

        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        val topOffset = dm.heightPixels - activity!!.layoutMain.measuredHeight

        val outPosition = IntArray(2)
        origin.getLocationInWindow(outPosition)

        copy.x = outPosition[0].toFloat()
        copy.y = outPosition[1].toFloat() - topOffset

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            copy.elevation = 20f
        }

        val ax = ObjectAnimator.ofFloat(copy, "x", receiptPosition[0] - 10f)
        val ay = ObjectAnimator.ofFloat(copy, "y", receiptPosition[1] - (topOffset + 10f))
        val asx = ObjectAnimator.ofFloat(copy, "scaleX", 0.2f)
        val asy = ObjectAnimator.ofFloat(copy, "scaleY", 0.2f)

        val animSet = AnimatorSet()
        animSet.duration = 500
        animSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                CheckOutItemsHolder.add(itemVO.copy())
                activity!!.layoutMain.removeView(copy)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
        animSet.playTogether(ax, ay, asx, asy)

        val w = (50 * dm.density).roundToInt()
        activity!!.layoutMain.addView(copy, ViewGroup.LayoutParams(w, w))
        animSet.start()

    }

    fun updateBadgeCount() {

        val badge: BadgeDrawable

        val reuse =  icon?.findDrawableByLayerId(R.id.ic_badge)
        if (reuse != null && reuse is BadgeDrawable){
            badge = reuse
        } else {
            badge = BadgeDrawable(context!!)
            val app = activity as MainActivity
            app.lockDrawer()
        }

        icon?.mutate()
        icon?.setDrawableByLayerId(R.id.ic_badge, badge)

        badge.setCount(CheckOutItemsHolder.itemCount.toString())

    }

}