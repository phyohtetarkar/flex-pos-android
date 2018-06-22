package com.jsoft.pos.ui.views.receipt

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
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.data.entity.TaxAmount
import com.jsoft.pos.databinding.ReceiptSlipBinding
import com.jsoft.pos.ui.custom.CustomViewAdapter
import com.jsoft.pos.ui.utils.ContextWrapperUtil
import com.jsoft.pos.ui.utils.FileUtil
import com.jsoft.pos.ui.utils.LockHandler
import com.jsoft.pos.ui.views.sale.CheckoutActivity
import kotlinx.android.synthetic.main.activity_receipt_detail.*

class ReceiptDetailActivity : AppCompatActivity() {

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
                    imageViewReceipt.setImageURI(it)
                }
            } else {
                binding.sale = it
                receiptItemAdapter.submitList(it?.saleItems)
                groupTaxAdapter.submitList(it?.groupTaxes)

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
            val uri = FileUtil.readReceipt(this, viewModel.sale.value?.receipt)

            uri?.also {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ibelieveinlove12@gmail.com"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Flex POS Receipt")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                emailIntent.type = "image/*"
                emailIntent.putExtra(Intent.EXTRA_STREAM, it)

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

        LockHandler.navigated(this, false)

    }

    override fun onBackPressed() {
        LockHandler.navigated(this, true)
        super.onBackPressed()
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
                LockHandler.navigated(this, true)
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("id", viewModel.saleId.value)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}