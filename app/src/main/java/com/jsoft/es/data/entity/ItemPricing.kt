package com.jsoft.es.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    (ForeignKey(entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["item_id"])),
    (ForeignKey(entity = Unit::class,
            parentColumns = ["id"],
            childColumns = ["unit_id"]))
], indices = [
    (Index(value = ["unit_id"])),
    (Index(value = ["item_id"]))
], tableName = "item_pricing")
data class ItemPricing(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var size: Double = 0.00,
        var price: Double = 0.00,
        @ColumnInfo(name = "unit_id")
        var unitId: Int = 0,
        @ColumnInfo(name = "item_id")
        var itemId: Long = 0,

        @Ignore
        var unit: Unit = Unit(name = "choose")
)