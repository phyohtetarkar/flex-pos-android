package com.flex.pos.ui.views.receipt

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.flex.pos.R
import com.flex.pos.data.entity.SaleItem
import com.flex.pos.data.entity.TaxAmount
import com.flex.pos.databinding.ReceiptSlipBinding
import com.flex.pos.ui.custom.CustomViewAdapter
import com.flex.pos.ui.utils.ContextWrapperUtil
import com.flex.pos.ui.utils.FileUtil
import com.flex.pos.ui.views.lock.AutoLockActivity
import com.flex.pos.ui.views.sale.CheckoutActivity
import kotlinx.android.synthetic.main.activity_receipt_detail.*

class ReceiptDetailActivity : AutoLockActivity() {

    private lateinit var viewModel: ReceiptDetailViewModel
    private var historyMode = false

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ReceiptSlipBinding>(this, R.layout.activity_receipt_detail)
        binding.setLifecycleOwner(this)

        historyMode = intent.getBooleanExtra("historyMode", false)

        supportActionBar?.setDisplayHomeAsUpEnabled(historyMode)
        supportActionBar?.setTitle(R.string.receipt_detail)

        if (historyMode) {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_dark)
            imageViewReceipt.visibility = View.VISIBLE
            constLayoutReceipt.visibility = View.GONE
        } else {
            imageViewReceipt.visibility = View.GONE
            constLayoutReceipt.visibility = View.VISIBLE
        }

        viewModel = ViewModelProviders.of(this).get(ReceiptDetailViewModel::class.java)

        val receiptItemAdapter = object : CustomViewAdapter<SaleItem>(linearLayoutReceiptItems, R.layout.layout_receipt_item) {
            override fun onBindView(holder: SimpleViewHolder, position: Int) {
                holder.bind(list[position])
            }
        }

        val groupTaxAdapter = object : CustomViewAdapter<TaxAmount>(linearLayoutGroupTaxes, R.layout.layout_receipt_tax_small) {
            override fun onBindView(holder: SimpleViewHolder, position: Int) {
                holder.bind(list[position])
            }
        }

        viewModel.sale.observe(this, Observer {
            if (historyMode) {
                FileUtil.readReceipt(this, it?.receipt)?.also {
                    imageViewReceipt.setImageBitmap(it)
                }
            } else {
                binding.sale = it
                receiptItemAdapter.submitList(it?.saleItems)
                groupTaxAdapter.submitList(it?.groupTaxes)

                tvReceiptHeader.text = PreferenceManager.getDefaultSharedPreferences(this)
                        .getString("p_shop_name", resources.getString(R.string.app_name))

                Handler().postDelayed({
                    val w = constLayoutReceipt.width
                    val h = constLayoutReceipt.height

                    val b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

                    val cv = Canvas(b)
                    cv.drawColor(Color.WHITE)
                    constLayoutReceipt.draw(cv)

                    it?.receipt = FileUtil.generateReceipt(this, b, it?.receipt)
                    viewModel.update()
                }, 2000)
            }
        })

        fabSendReceipt.setOnClickListener {
            val uri = FileUtil.getReceiptUri(this, viewModel.sale.value?.receipt)

            uri?.also {
                val emailIntent = Intent(Intent.ACTION_SEND)
                PreferenceManager.getDefaultSharedPreferences(this)
                        .getString("p_mail_subject", "Flex POS").also {
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, it)
                        }
                emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                emailIntent.type = "image/*"
                emailIntent.putExtra(Intent.EXTRA_STREAM, it)
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                navigated = true

                startActivity(Intent.createChooser(emailIntent, "Send Receipt With"))
            }

        }

        viewModel.saleId.value = intent.getLongExtra("id", 0)

        scrollViewSendReceipt.viewTreeObserver.addOnScrollChangedListener {
            if (scrollViewSendReceipt.scrollY > 0) {
                if (fabSendReceipt.isShown) {
                    fabSendReceipt.hide()
                }
            } else {
                if (!fabSendReceipt.isShown) {
                    fabSendReceipt.show()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (!historyMode) {
            menuInflater.inflate(R.menu.menu_receipt_detail, menu)
        } else {
            menuInflater.inflate(R.menu.menu_receipt_detail_edit, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_new_sale -> onBackPressed()
            R.id.action_edit_sale -> {
                navigated = true
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("id", viewModel.saleId.value)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}