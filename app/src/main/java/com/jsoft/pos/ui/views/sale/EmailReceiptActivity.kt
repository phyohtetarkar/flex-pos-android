package com.jsoft.pos.ui.views.sale

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.databinding.ReceiptSlipBinding
import com.jsoft.pos.ui.custom.CustomViewAdapter
import com.jsoft.pos.ui.utils.ImageUtil
import kotlinx.android.synthetic.main.fragment_send_receipt.*

class EmailReceiptActivity : AppCompatActivity() {

    private lateinit var viewModel: EmailReceiptViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ReceiptSlipBinding>(this, R.layout.fragment_send_receipt)
        binding.setLifecycleOwner(this)

        viewModel = ViewModelProviders.of(this).get(EmailReceiptViewModel::class.java)

        val receiptItemAdapter = object : CustomViewAdapter<SaleItem>(linearLayoutReceiptItems, R.layout.layout_receipt_item) {
            override fun onBindView(holder: SimpleViewHolder, position: Int) {
                holder.bind(list[position])
            }

        }



        viewModel.sale.observe(this, Observer {
            binding.sale = it
            receiptItemAdapter.submitList(it?.saleItems)

            constLayoutReceipt.isDrawingCacheEnabled = true

            ImageUtil.writeImage(this@EmailReceiptActivity, constLayoutReceipt.drawingCache)
        })

    }

    override fun onResume() {
        super.onResume()

        viewModel.saleId.value = intent.getLongExtra("id", 0)
    }

}