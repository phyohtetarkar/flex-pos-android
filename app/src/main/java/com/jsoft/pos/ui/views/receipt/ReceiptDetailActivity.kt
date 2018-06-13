package com.jsoft.pos.ui.views.receipt

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.data.entity.TaxAmount
import com.jsoft.pos.databinding.ReceiptSlipBinding
import com.jsoft.pos.ui.custom.CustomViewAdapter
import com.jsoft.pos.ui.utils.ImageUtil
import kotlinx.android.synthetic.main.activity_receipt_detail.*

class ReceiptDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ReceiptDetailViewModel
    private var showHomeUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ReceiptSlipBinding>(this, R.layout.activity_receipt_detail)
        binding.setLifecycleOwner(this)

        showHomeUp = intent.getBooleanExtra("showHomeUp", false)

        supportActionBar?.setDisplayHomeAsUpEnabled(showHomeUp)

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
            binding.sale = it
            receiptItemAdapter.submitList(it?.saleItems)
            groupTaxAdapter.submitList(it?.groupTaxes)
        })

        fabSendReceipt.setOnClickListener {
            val w = constLayoutReceipt.width
            val h = constLayoutReceipt.height

            val b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

            val cv = Canvas(b)
            cv.drawColor(Color.WHITE)
            constLayoutReceipt.draw(cv)

            val uri = ImageUtil.generateReceipt(this@ReceiptDetailActivity, b)
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ibelieveinlove12@gmail.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Flex POS Receipt")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "")
            emailIntent.type = "image/*"
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri)

            startActivity(Intent.createChooser(emailIntent, "Send Receipt With"))
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}