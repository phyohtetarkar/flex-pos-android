package com.jsoft.pos.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Sale::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"]),
    ForeignKey(entity = Charge::class,
            parentColumns = ["id"],
            childColumns = ["charge_id"])
], indices = [
    Index(value = ["sale_id"]),
    Index(value = ["discount_id"])
], tableName = "sale_discount")
data class SaleCharge(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        @ColumnInfo(name = "sale_id")
        var saleId: Long = 0,
        @ColumnInfo(name = "charge_id")
        var chargeId: Int = 0,
        var amount: Double = 0.0,
        var percentage: Boolean = true
) {
    @Ignore
    var charge: Charge? = null
}