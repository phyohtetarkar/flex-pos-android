package com.jsoft.es.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Unit::class,
            parentColumns = ["id"],
            childColumns = ["unit_id"]),
    ForeignKey(entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"])
], indices = [
    (Index(value = ["unit_id"])),
    (Index(value = ["category_id"]))
])
data class Item(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String = "",
        var code: String = "",
        var barcode: String = "",
        var image: String = "",
        var remark: String = "",
        var amount: Double = 0.0,
        @ColumnInfo(name = "retail_price")
        var retailPrice: Int = 0,
        @ColumnInfo(name = "purchase_price")
        var purchasePrice: Int = 0,
        var discount: Int = 0,
        @ColumnInfo(name = "discount_flag")
        var discountFlag: Boolean = false,
        var available: Boolean = true,
        @ColumnInfo(name = "unit_id")
        var unitId: Int = 0,
        @ColumnInfo(name = "category_id")
        var categoryId: Int = 0,

        @Ignore
        var unit: Unit? = Unit(name = "choose"),
        @Ignore
        var category: Category? = Category(name = "choose")
)

data class ItemVO(
        var id: Long,
        var name: String,
        var code: String,
        var image: String,
        var amount: Double,
        @ColumnInfo(name = "retail_price")
        var price: Int,
        var discount: Int,
        @ColumnInfo(name = "discount_flag")
        var discountFlag: Boolean,
        var unit: String,
        var category: String,
        var color: String
) {

        val rate: String
            @Ignore get() {
                return if ((amount - amount.toInt()) % 10 == 0.0) {
                    "(${amount.toInt()} $unit)"
                } else {
                    "($amount $unit)"
                }
            }
}