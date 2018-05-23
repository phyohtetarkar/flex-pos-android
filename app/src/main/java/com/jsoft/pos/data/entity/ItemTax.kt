package com.jsoft.pos.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["item_id"]),
    ForeignKey(entity = Tax::class,
            parentColumns = ["id"],
            childColumns = ["tax_id"])
], indices = [
    Index(value = ["item_id"]),
    Index(value = ["tax_id"])
], tableName = "item_tax")
data class ItemTax(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        @ColumnInfo(name = "item_id")
        var itemId: Long = 0,
        @ColumnInfo(name = "tax_id")
        var taxId: Int = 0
)