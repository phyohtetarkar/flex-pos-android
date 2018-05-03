package com.jsoft.es.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    (ForeignKey(entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["item_id"])),
    (ForeignKey(entity = Sale::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"]))
], indices = [
    (Index(value = ["item_id"])),
    (Index(value = ["sale_id"]))
])
data class SaleItem(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var quantity: Int = 0,
        @ColumnInfo(name = "purchase_price")
        var purchasePrice: Int = 0,
        @ColumnInfo(name = "unit_price")
        var unitPrice: Int = 0,
        var discount: Int = 0,
        var remark: String = "",
        @ColumnInfo(name = "item_id")
        var itemId: Int = 0,
        @ColumnInfo(name = "sale_id")
        var saleId: Long = 0
)