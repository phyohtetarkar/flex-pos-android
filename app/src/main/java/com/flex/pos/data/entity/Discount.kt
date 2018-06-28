package com.flex.pos.data.entity

import android.arch.persistence.room.*

@Entity(indices = [(Index(value = ["unique_name"], unique = true))])
data class Discount(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var name: String = "",
        var amount: Double = 0.00,
        var percentage: Boolean = true,
        @ColumnInfo(name = "unique_name")
        var uniqueName: String = ""
) : Checkable {

    @Ignore
    override var _name: String = ""
        get() {
            val u = if (percentage) "%" else ""
            return "$name (${amount.toSimplifyString()}$u)"
        }

    @Ignore
    override var _checked: Boolean = false

    val discountDesc: String
        get() {
            val u = if (percentage) "%" else ""

            return "${amount.toSimplifyString()} $u"
        }
}