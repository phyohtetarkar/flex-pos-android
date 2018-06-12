package com.jsoft.pos.data.entity

import android.arch.persistence.room.*
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.jsoft.pos.BR
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

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

    companion object {
        fun create(list: List<SaleItem>?): Sale {

            if (list.orEmpty().isEmpty()) {
                return Sale()
            }

            val sale = Sale(totalItem = list?.size ?: 0,
                    discount = calculateDiscount(list).round(),
                    subTotalPrice = list?.map { it.total }?.sum()?.round() ?: 0.00)

            val taxIn = calculateInclusiveTax(list)
            val taxEx = calculateExclusiveTax(list)

            sale.taxAmount = taxIn.plus(taxEx).round()
            sale.totalPrice = sale.subTotalPrice.minus(sale.discount)
                    .plus(taxEx)
                    .round()

            return sale
        }

        private fun calculateDiscount(list: List<SaleItem>?): Double {
            return list?.sumByDouble { it.computedDiscount } ?: 0.0
        }

        private fun calculateInclusiveTax(list: List<SaleItem>?): Double {
            return list?.map {
                val amount = it.total.minus(it.computedDiscount)

                amount.div(
                        it.item?.taxes?.filter { it.included }
                                ?.map { it.amount.div(100) }
                                ?.sum()?.plus(1) ?: 0.0
                ).minus(amount).absoluteValue
            }?.sum() ?: 0.0
        }

        private fun calculateExclusiveTax(list: List<SaleItem>?): Double {
            return list?.map {
                it.item?.taxes?.filter { !it.included }
                        ?.map { it.amount.div(100) }
                        ?.sum()?.times(it.total.minus(it.computedDiscount)) ?: 0.0
            }?.sum() ?: 0.0
        }

    }

}