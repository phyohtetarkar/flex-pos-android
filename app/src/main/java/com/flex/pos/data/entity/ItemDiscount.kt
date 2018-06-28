package com.flex.pos.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["item_id"]),
    ForeignKey(entity = Discount::class,
            parentColumns = ["id"],
            childColumns = ["discount_id"])
], indices = [
    Index(value = ["item_id"]),
    Index(value = ["discount_id"])
], tableName = "item_discount")
data class ItemDiscount(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        @ColumnInfo(name = "item_id")
        var itemId: Long = 0,
        @ColumnInfo(name = "discount_id")
        var discountId: Int = 0
)