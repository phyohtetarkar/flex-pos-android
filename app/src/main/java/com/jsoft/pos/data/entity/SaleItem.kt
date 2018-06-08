package com.jsoft.pos.data.entity

import android.arch.persistence.room.*

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
        var itemId: Long? = null,
        @ColumnInfo(name = "sale_id")
        var saleId: Long? = null
) {
    @Ignore
    var item: Item? = null

    @Ignore
    var total: Double = 0.00
        get() = quantity * price

    @Ignore
    var computedDiscount: Double = 0.00
        get() = item?.discounts?.sumByDouble {
                if (it.percentage) {
                    it.amount.div(100)
                } else {
                    it.amount.times(100).div(price).div(100)
                }
            }?.times(total) ?: 0.00

    @Ignore
    var priceDesc: String = ""
        get() {
            return "$quantity \u00D7 ${item?.price?.toSimplifyString()}"
        }

    @Ignore
    var sizeDesc: String = ""
        get() {
            return "${item?.amount?.toSimplifyString()} ${item?.unit?.name}"
        }
}