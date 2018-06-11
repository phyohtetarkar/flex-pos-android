package com.jsoft.pos.data.entity

import android.arch.persistence.room.*
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.jsoft.pos.BR
import java.text.SimpleDateFormat
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
        @ColumnInfo(name = "charge_amount")
        var chargeAmount: Double = 0.0,
        @ColumnInfo(name = "sub_total_price")
        var subTotalPrice: Double = 0.0,
        @ColumnInfo(name = "total_price")
        var totalPrice: Double = 0.0,
        @ColumnInfo(name = "pay_price")
        var payPrice: Double = 0.0,
        @Bindable
        var change: Double = 0.0,
        var receipt: String = ""
) : BaseObservable() {

    @Ignore
    var _payPrice = payPrice
        set(value) {
            val v = value - totalPrice
            if (v > 0) {
                payPrice = value
                change = v
            } else {
                change = 0.0
            }

            notifyPropertyChanged(BR.change)

        }

    val _issueDate: String
        get() {
            val format = SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.ENGLISH)
            return format.format(issueDate)
        }

    @Ignore
    var saleItems: List<SaleItem> = mutableListOf()

    @Ignore
    var saleCharges: List<SaleCharge>? = null
    @Ignore
    var saleDiscounts: List<SaleDiscount>? = null

    companion object {
        fun create(list: List<SaleItem>?): Sale {

            if (list.orEmpty().isEmpty()) {
                return Sale()
            }

            val sale = Sale(totalItem = list?.size ?: 0,
                    discount = calculateDiscount(list).round(),
                    subTotalPrice = list?.map { it.total }?.sum()?.round() ?: 0.00)

            val chargeIn = calculateInclusiveCharge(list)
            val chargeEx = calculateExclusiveCharge(list)

            sale.totalPrice = sale.subTotalPrice.minus(sale.discount)

            val sDiscount = calculateSaleDiscount(sale.saleDiscounts, sale.totalPrice)
            sale.totalPrice -= sDiscount

            val sCharge = calculateSaleCharge(sale.saleCharges, sale.totalPrice)

            sale.chargeAmount = chargeIn.plus(chargeEx).plus(sCharge).round()

            sale.totalPrice = sale.totalPrice.plus(chargeEx).plus(sCharge).round()

            return sale
        }

        private fun calculateDiscount(list: List<SaleItem>?): Double {
            return list?.sumByDouble { it.computedDiscount } ?: 0.0
        }

        private fun calculateSaleDiscount(list: List<SaleDiscount>?, totalPrice: Double): Double {
            return list?.sumByDouble {
                if (it.percentage) {
                    it.amount.div(100)
                } else {
                    it.amount.times(100).div(totalPrice).div(100)
                }
            }?.times(totalPrice) ?: 0.0
        }

        private fun calculateSaleCharge(list: List<SaleCharge>?, totalPrice: Double): Double {
            return list?.sumByDouble {
                if (it.percentage) {
                    it.amount.div(100)
                } else {
                    it.amount.times(100).div(totalPrice).div(100)
                }
            }?.times(totalPrice) ?: 0.0
        }

        private fun calculateInclusiveCharge(list: List<SaleItem>?): Double {
            return list?.sumByDouble { it.computedInclusiveCharge } ?: 0.0
        }

        private fun calculateExclusiveCharge(list: List<SaleItem>?): Double {
            return list?.sumByDouble { it.computedExclusiveCharge } ?: 0.0
        }

    }

}