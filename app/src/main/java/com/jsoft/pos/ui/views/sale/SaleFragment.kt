package com.jsoft.pos.ui.views.sale

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.util.DiffUtil
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.data.model.ItemVOSearch
import com.jsoft.pos.ui.custom.BadgeDrawable
import com.jsoft.pos.ui.custom.RoundedImageView
import com.jsoft.pos.ui.views.BindingViewHolder
import com.jsoft.pos.ui.views.ListViewModel
import com.jsoft.pos.ui.views.SimpleListFragment
import com.jsoft.pos.ui.views.SimplePagedListAdapter
import kotlinx.android.synthetic.main.fragment_simple_list.*
import kotlinx.android.synthetic.main.layout_app_bar_main.*
import kotlinx.android.synthetic.main.layout_item_compact.view.*


class SaleFragment : SimpleListFragment<ItemVO>() {

    private lateinit var adapter: SimplePagedListAdapter<ItemVO>
    private lateinit var spinnerAdapter: ArrayAdapter<Category>
    private lateinit var viewModel: SaleViewModel
    private var mSpinner: AppCompatSpinner? = null
    private var icon: LayerDrawable? = null

    private val receiptPosition = IntArray(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SaleViewModel::class.java)
        adapter = SimplePagedListAdapter(object : DiffUtil.ItemCallback<ItemVO>() {
            override fun areItemsTheSame(oldItem: ItemVO?, newItem: ItemVO?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: ItemVO?, newItem: ItemVO?): Boolean {
                return oldItem == newItem
            }

        }, R.layout.layout_item_compact)

        setHasOptionsMenu(true)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.toolbarMain.title = null

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

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.toolbarMain?.removeView(mSpinner)
        mSpinner = null
        icon = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        recyclerViewSimpleList.removeItemDecorationAt(0)
//        recyclerViewSimpleList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).also {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                it.setDrawable(resources.getDrawable(R.drawable.padded_divider_82, resources.newTheme()))
//            } else {
//                it.setDrawable(resources.getDrawable(R.drawable.padded_divider_82))
//            }
//        })

        fabSimpleList.visibility = View.GONE

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.categories.observe(this, Observer {
            spinnerAdapter.clear()
            spinnerAdapter.add(Category(name = "All Item"))
            spinnerAdapter.addAll(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_sale, menu)

        icon = menu?.findItem(R.id.action_receipt)?.icon as LayerDrawable?

        Handler().post {
            val v = activity?.findViewById<View>(R.id.action_receipt)
            v?.getLocationOnScreen(receiptPosition)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.action_receipt -> {
                val intent = Intent(context, CheckoutActivity::class.java)
                intent.putExtra("itemIds", viewModel.checkedItemIds.toLongArray())
                startActivity(intent)
            }

        }

        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.itemSearch.value = ItemVOSearch()
    }

    override val _viewModel: ListViewModel<ItemVO>
        get() = viewModel

    override val _adapter: RecyclerView.Adapter<BindingViewHolder>
        get() = adapter

    override fun showEdit(id: Any) {

    }

    override fun onItemTouch(position: Int) {

    }

    override fun onItemTouch(view: View, position: Int) {

        val itemVO = adapter.getItemAt(position)

        val origin = view.roundedImageView
        val bitmapDrawable: BitmapDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resources.getDrawable(R.drawable.ic_placeholder, null) as BitmapDrawable
        } else {
            resources.getDrawable(R.drawable.ic_placeholder) as BitmapDrawable
        }
        val copy = RoundedImageView(activity!!)
        copy.setImageBitmap(bitmapDrawable.bitmap)
        copy.layoutParams = origin.layoutParams

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
        animSet.duration = 600
        animSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                viewModel.checkedItemIds.add(itemVO.id)
                updateBadgeCount()
                activity!!.layoutMain.removeView(copy)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                activity!!.layoutMain.addView(copy)
            }

        })
        animSet.playTogether(ax, ay, asx, asy)
        animSet.start()

    }

    fun updateBadgeCount() {

        val badge: BadgeDrawable

        // Reuse drawable if possible
        val reuse = icon?.findDrawableByLayerId(R.id.ic_badge)
        if (reuse != null && reuse is BadgeDrawable) {
            badge = reuse
        } else {
            badge = BadgeDrawable(activity!!)
        }

        badge.setCount(viewModel.checkedItemIds.size.toString())
        icon?.mutate()
        icon?.setDrawableByLayerId(R.id.ic_badge, badge)
    }

    companion object {
        val INSTANCE: SaleFragment
            get() {
                return SaleFragment()
            }
    }

}