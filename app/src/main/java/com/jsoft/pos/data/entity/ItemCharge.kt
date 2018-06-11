package com.jsoft.pos.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["item_id"]),
    ForeignKey(entity = Charge::class,
            parentColumns = ["id"],
            childColumns = ["charge_id"])
], indices = [
    Index(value = ["item_id"]),
    Index(value = ["charge_id"])
], tableName = "item_charge")
data class ItemCharge(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        @ColumnInfo(name = "item_id")
        var itemId: Long = 0,
        @ColumnInfo(name = "charge_id")
        var chargeId: Int = 0
)