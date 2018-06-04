package com.jsoft.pos.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class Discount(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var name: String = "",
        var amount: Double = 0.00,
        var percentage: Boolean = true
) : Checkable {

    @Ignore
    override var _name: String = ""
        get() = name

    @Ignore
    override var _checked: Boolean = false

    val discountDesc: String
        get() {
            val u = if (percentage) "%" else ""

            return if ((amount - amount.toInt()) % 10 == 0.0) {
                "${amount.toInt()} $u"
            } else {
                "$amount $u"
            }
        }
}