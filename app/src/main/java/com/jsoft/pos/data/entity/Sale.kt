package com.jsoft.pos.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

@Entity(indices = [(Index(value = ["receipt_code"], unique = true))])
data class Sale(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        @ColumnInfo(name = "receipt_code")
        var receiptCode: String = "",
        @ColumnInfo(name = "issue_date")
        var issueDate: Date = Date(),
        @ColumnInfo(name = "total_item")
        var totalItem: Int = 0,
        var discount: Double = 0.0,
        @ColumnInfo(name = "tax_amount")
        var taxAmount: Double = 0.0,
        @ColumnInfo(name = "sub_total_price")
        var subTotalPrice: Double = 0.0,
        @ColumnInfo(name = "total_price")
        var totalPrice: Double = 0.0,
        @ColumnInfo(name = "pay_price")
        var payPrice: Double = 0.0,
        var change: Double = 0.0,
        var receipt: String = ""
) {
    companion object {
        fun create(list: List<SaleItem>?): Sale {

            if (list.orEmpty().isEmpty()) {
                return Sale()
            }

            val df = DecimalFormat("#.####")
            df.roundingMode = RoundingMode.HALF_UP

            val sale = Sale(totalItem = list?.size ?: 0,
                    discount = calculateDiscount(list).round("#.####"),
                    subTotalPrice = list?.map { it.total }?.sum()?.round("#.####") ?: 0.00)

            sale.taxAmount = calculateTax(list).round("#.####")
            sale.totalPrice = sale.subTotalPrice.minus(sale.discount)
                    .plus(sale.taxAmount)
                    .round("#.####")

            return sale
        }

        private fun calculateDiscount(list: List<SaleItem>?): Double {
            return list?.sumByDouble { it.computedDiscount } ?: 0.0
        }

        private fun calculateTax(list: List<SaleItem>?): Double {
            return list?.map {
                it.item?.taxes?.map {
                    if (it.included) {
                        it.amount.div(100)
                    } else {
                        0.0
                    }
                }?.sum()?.times(it.total.minus(it.computedDiscount)) ?: 0.0
            }?.sum() ?: 0.0
        }

    }

}