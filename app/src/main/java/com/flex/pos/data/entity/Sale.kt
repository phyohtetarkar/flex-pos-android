package com.flex.pos.data.entity

import android.arch.persistence.room.*
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flex.pos.BR
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
        var receipt: String? = null
) : BaseObservable() {

    @Ignore
    var _payPrice = payPrice
        set(value) {

            val v = value - totalPrice
            if (v > -1) {
                payPrice = value
            }

            change = v

            notifyPropertyChanged(BR.change)

        }

    val _issueDate: String
        get() {
            val format = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.ENGLISH)
            return format.format(issueDate)
        }

    @Ignore
    var saleItems = mutableListOf<SaleItem>()
        set(value) {
            field = value

            totalItem = value.sumBy { it.quantity }
            discount = value.sumByDouble { it.computedDiscount }.round()
            subTotalPrice = value.sumByDouble { it.total }.round()

            val taxIn = value.sumByDouble { it.computedInclusiveCharge }
            val taxEx = value.sumByDouble { it.computedExclusiveCharge }

            taxAmount = taxIn.plus(taxEx).round()
            totalPrice = subTotalPrice.minus(discount)
                    .plus(taxEx)
                    .round()

            notifyChange()
        }

    val groupTaxes: List<TaxAmount>
        get() = saleItems.flatMap {
            val price = it.total.minus(it.computedDiscount)
            return@flatMap it.item?.taxes?.map {
                if (it.included) {
                    return@map TaxAmount(it.name,
                            price.div(it.amount.div(100).plus(1))
                                    .minus(price).absoluteValue)
                } else {
                    return@map TaxAmount(it.name, it.amount.div(100).times(price))
                }
            }?.toList() ?: listOf()
        }.groupBy { it.name }.map { en ->
            val amount = en.value.sumByDouble { it.amount }
            return@map TaxAmount(en.key, amount)
        }
}

data class TaxAmount(
        var name: String,
        var amount: Double
)