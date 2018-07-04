package com.flex.pos.ui.views.sale

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.ObservableList
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.flex.pos.MainActivity
import com.flex.pos.R
import com.flex.pos.data.entity.Category
import com.flex.pos.data.entity.ItemVO
import com.flex.pos.data.entity.SaleItem
import com.flex.pos.data.model.ItemVOSearch
import com.flex.pos.ui.custom.RoundedImageView
import com.flex.pos.ui.utils.AlertUtil
import com.flex.pos.ui.utils.Utils
import com.flex.pos.ui.views.BindingViewHolder
import com.flex.pos.ui.views.ListViewModel
import com.flex.pos.ui.views.SimpleListFragment
import com.flex.pos.ui.views.SimplePagedListAdapter
import com.flex.pos.ui.views.barcode.BarcodeGraphicTracker
import com.flex.pos.ui.views.barcode.FragmentScannerSheet
import com.flex.pos.ui.views.item.ItemVOAdapter
import com.flex.pos.ui.views.lock.AutoLockActivity
import com.google.android.gms.vision.barcode.Barcode
import kotlinx.android.synthetic.main.fragment_simple_list.*
import kotlinx.android.synthetic.main.layout_app_bar_main.*
import kotlinx.android.synthetic.main.layout_item_compact.view.*
import kotlin.math.roundToInt

class SaleFragment : SimpleListFragment<ItemVO>(), BarcodeGraphicTracker.BarcodeDetectorDelegate {

    private val CAMERA_PERMISSION_FOR_SCAN = 1;
    private val SCANNER_SHEET_TAG = "scannerSheet"

    private lateinit var adapter: SimplePagedListAdapter<ItemVO>
    private lateinit var spinnerAdapter: ArrayAdapter<Category>
    private lateinit var viewModel: SaleViewModel
    private var mSpinner: AppCompatSpinner? = null

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

        val notiLayout = menu?.findItem(R.id.action_receipt)?.actionView
        notiLayout?.setOnClickListener {
            if (CheckOutItemsHolder.itemCount > 0) {
                (activity as? AutoLockActivity)?.navigated = true
                val intent = Intent(context, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }

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
            R.id.action_scan -> {
                if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    showScannerSheet()
                } else {
                    ActivityCompat.requestPermissions(activity!!,
                            arrayOf(Manifest.permission.CAMERA),
                            CAMERA_PERMISSION_FOR_SCAN)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_FOR_SCAN -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showScannerSheet()
                } else {
                    AlertUtil.showToast(context, "Permission denied, cannot access camera")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.itemSearch.value = ItemVOSearch().also { it.isAvailable = true }

        val app = activity as MainActivity

        if (!CheckOutItemsHolder.onSale) {
            app.unlockDrawer()
        }

        updateBadgeCount()
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
        activity?.layoutMain?.addView(copy, ViewGroup.LayoutParams(w, w))
        animSet.start()

    }

    override fun onBarcodeDetected(b: Barcode?) {
        Log.v("TAG", "Detected: ${b?.rawValue}")
    }

    private fun updateBadgeCount() {

        val count = CheckOutItemsHolder.itemCount.toString()

        val item = activity?.toolbarMain?.menu?.findItem(R.id.action_receipt)
        val tv = item?.actionView?.findViewById<TextView>(R.id.notification_count)

        if (count.equals("0", ignoreCase = true)) {
            tv?.visibility = View.GONE
        } else {
            if (count.length > 2) {
                tv?.text = resources.getString(R.string._99_plus)
            } else {
                tv?.text = count
            }

            tv?.visibility = View.VISIBLE
        }
    }

    private fun showScannerSheet() {
        val ft = fragmentManager?.beginTransaction();
        val prev = fragmentManager?.findFragmentByTag(SCANNER_SHEET_TAG)

        if (prev != null) {
            ft?.remove(prev)
        }

        val frag = FragmentScannerSheet()
        frag.arguments = Bundle().also {
            it.putBoolean("hasParentFragment", true)
        }
        frag.show(ft, SCANNER_SHEET_TAG)

    }

}