package com.jsoft.pos.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Sale::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"])
], indices = [
    Index(value = ["sale_id"])
], tableName = "sale_item")
data class SaleItem(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var item: String = "",
        var unit: String = "",
        var quantity: Int = 0,
        var price: Double = 0.00,
        var category: String = "",
        var remark: String = "",

        @ColumnInfo(name = "sale_id")
        var saleId: Long = 0
)