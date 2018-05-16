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
    Index(value = ["unit_id"]),
    Index(value = ["category_id"])
])
data class Item(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String = "",
        var barcode: String = "",
        var amount: Double = 0.0,
        var price: Double = 0.0,
        var image: String = "",
        var remark: String = "",
        var available: Boolean = true,
        @ColumnInfo(name = "unit_id")
        var unitId: Int = 0,
        @ColumnInfo(name = "category_id")
        var categoryId: Int = 0
) {
    @Ignore
    var unit: Unit? = Unit(name = "choose")
        set(value) {
            field = value
            value?.id?.apply { unitId = this }
        }

    @Ignore
    var category: Category? = Category(name = "choose")
        set(value) {
            field = value
            value?.id?.apply { categoryId = this }
        }
}

data class ItemVO(
        var id: Long,
        var name: String,
        var barcode: String,
        var image: String,
        var unit: String,
        var category: String,
        var color: String,
        var amount: Double,
        var price: Double
) {
    @Ignore
    var amountDesc: String? = null
        get() {
            if ((amount - amount.toInt()) % 10 == 0.0) {
                return "${amount.toInt()} $unit"
            } else {
                return "$amount $unit"
            }
        }
}