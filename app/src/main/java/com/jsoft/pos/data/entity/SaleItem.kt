package com.jsoft.pos.data.entity

import android.arch.persistence.room.*
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.jsoft.pos.BR
import kotlin.math.absoluteValue

@Entity(foreignKeys = [
    ForeignKey(entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["item_id"]),
    ForeignKey(entity = Sale::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"])
], indices = [
    Index(value = ["item_id"]),
    Index(value = ["sale_id"])
], tableName = "sale_item")
data class SaleItem(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var quantity: Int = 0,
        var price: Double = 0.00,
        var remark: String = "",

        @ColumnInfo(name = "item_id")
        var itemId: Long = 0,
        @ColumnInfo(name = "sale_id")
        var saleId: Long = 0
) : BaseObservable() {
    @Ignore
    var item: Item? = null

    @Bindable
    @Ignore
    var total: Double = 0.00
        get() = quantity * price

    @Ignore
    var _quantity = quantity
        set(value) {
            quantity = if (value > 0) value else 1
            notifyPropertyChanged(BR.total)
        }

    val computedDiscount: Double
        get() = item?.discounts?.sumByDouble {
            if (it.percentage) {
                it.amount.div(100)
            } else {
                it.amount.times(100).div(price).div(100)
            }
        }?.times(total) ?: 0.00

    val computedInclusiveCharge: Double
        get() {
            val amount = total.minus(computedDiscount)

            return amount.div(item?.taxes?.filter { it.included }
                    ?.sumByDouble { it.amount.div(100) }
                    ?.plus(1) ?: 0.0
            ).minus(amount).absoluteValue
        }

    val computedExclusiveCharge: Double
        get() {
            val amount = total.minus(computedDiscount)

            return item?.taxes?.filter { !it.included }
                    ?.sumByDouble { it.amount.div(100) }
                    ?.times(amount) ?: 0.0
        }

    val priceDesc: String
        get() {
            return "$quantity \u00D7 ${item?.price?.toSimplifyString()}"
        }

    val sizeDesc: String
        get() {
            return "${item?.amount?.toSimplifyString()} ${item?.unit?.name}"
        }

    val itemDesc: String
        get() {
            return "${item?.name} $sizeDesc @${price.toSimplifyString()}"
        }
}