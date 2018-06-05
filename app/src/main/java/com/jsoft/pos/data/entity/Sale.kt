package com.jsoft.pos.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
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
        var discount: Double = 0.00,
        @ColumnInfo(name = "tax_amount")
        var taxAmount: Double = 0.00,
        @ColumnInfo(name = "sub_total_price")
        var subTotalPrice: Double = 0.00,
        @ColumnInfo(name = "total_price")
        var totalPrice: Double = 0.00,
        @ColumnInfo(name = "pay_price")
        var payPrice: Double = 0.00,
        var change: Double = 0.00,
        var receipt: String = ""
) {
    companion object {
        fun create(list: List<SaleItem>?): Sale {
            return Sale(totalItem = list?.size ?: 0).also {
                it.discount = calculateDiscount(list)
                it.taxAmount = calculateTax(list)
                it.subTotalPrice = list?.map { it.total }?.sum() ?: 0.00
                it.totalPrice = it.subTotalPrice * it.discount * it.taxAmount
            }
        }

        private fun calculateDiscount(list: List<SaleItem>?): Double {
            return list?.map {
                it.item?.discounts?.map {
                    if (it.percentage) it.amount / 100 else it.amount
                }?.sum() ?: 0.00
            }?.sum() ?: 0.00
        }

        private fun calculateTax(list: List<SaleItem>?): Double {
            return list?.map {
                it.item?.taxes?.map {
                    if (it.included) it.amount / 100 else 0.00
                }?.sum() ?: 0.00
            }?.sum() ?: 0.00
        }

    }

}